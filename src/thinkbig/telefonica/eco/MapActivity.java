package thinkbig.telefonica.eco;

import android.content.*;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.neosistec.utils.logmanager.LogManager;
import thinkbig.telefonica.eco.adapter.PopupAdapter;
import thinkbig.telefonica.eco.database.GestorHospitales;
import thinkbig.telefonica.eco.modelo.DireccionMapa;
import thinkbig.telefonica.eco.modelo.Hospital;
import thinkbig.telefonica.eco.service.LocationService;

import java.util.ArrayList;


public class MapActivity extends SherlockFragmentActivity {
    private final static String TAG = MapActivity.class.getName();

    private GoogleMap map;
    private Location currentLocation;
    private Marker posicionActual;
    private Polyline polyline;
    private Marker markerActual;
    private String distancia;


    private LocationService locationService;

    private LocationReceiver locationReceiver = new LocationReceiver();

    private LogManager logManager = new LogManager(MapActivity.class.getName());

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.logo_small);
        actionBar.setTitle(getString(R.string.hospitales_cercanos_title));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();

        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter(EcoApplication.LOCATION_KEY));

    }


    /**
     * El locationClient luego habrá que ponerlo en un servicio aparte.
     */
    private void init() {

        /*if (locationClient == null) {
            locationClient = new LocationClient(this, mLocationCallback, mLocationCallback);
            Log.v(TAG, "Location Client connect");
            if (!(locationClient.isConnected() || locationClient.isConnecting())) {
                locationClient.connect();
            }
        }*/

        //Bindamos el servicio de localización también a esta actividad
        Intent intent = new Intent(MapActivity.this, LocationService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

        //Si no hay mapa lo creamos
        if (map == null) {
            FragmentManager myFragmentManager = getSupportFragmentManager();
            SupportMapFragment myMapFragment =
                    (SupportMapFragment) myFragmentManager.findFragmentById(R.id.mapa);
            map = myMapFragment.getMap();

            map.setInfoWindowAdapter(new PopupAdapter(MapActivity.this));
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Hospital hospital = GestorHospitales.getInstance(MapActivity.this).getHospitalByLatLng(marker
                            .getPosition());
                    if (hospital != null && hospital.getTelefono() != null) {
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + hospital.getTelefono()));
                        startActivity(intent);
                    }
                }
            });
        }
        //Una vez inicializamos el mapa empezamos a trabajar con él
        if (map!=null) {
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setZoomGesturesEnabled(true);
            if (currentLocation!=null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                        .zoom(13)
                        .build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                posicionActual = map.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(),
                        currentLocation.getLongitude())));
                posicionActual.setTitle("Está usted aquí");
            }

            //TODO Poner esto en un asynctask
            ArrayList<Hospital> listaHospitales = GestorHospitales.getInstance(this).getHospitales();
            for (Hospital hospital : listaHospitales) {
                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(hospital.getLatitud(),
                        hospital.getLongitud())));
                marker.setTitle(hospital.getNombre());
                String snippet = hospital.getDireccion() + "\n" + hospital.getCiudad() + "\n" + hospital.getTelefono();
                marker.setSnippet(snippet);

                BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.hospital_small);
                marker.setIcon(bm);
            }


            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (!marker.equals(posicionActual)) {
                        showDistance(marker);
                    }
                    return false;
                }
            });
        }

        /*Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName("hospitales", 500, 37.37, -2.42,38.66, 0.05);
            for (Address address : addresses) {
                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),
                        address.getLongitude())));
                marker.setTitle(address.getFeatureName());
                String snippet = address.getAddressLine(0) + "\n" + address.getLocality() + "\n" + address.getPhone();
                marker.setSnippet(snippet);

                BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.hospital_small);
                marker.setIcon(bm);
            }
        } catch (IOException e) {
            logManager.error("EXCEPTION: " + e.getMessage());
        }*/
    }

    private void showDistance(Marker marker) {
        if (polyline != null) {
            polyline.remove();
        }
        DireccionMapa direccionMapa = new DireccionMapa(new LatLng(currentLocation.getLatitude(),
                currentLocation.getLongitude()),
                marker.getPosition());

        if (markerActual != null) {
            markerActual.setSnippet(markerActual.getSnippet().replace(distancia, ""));
        }
        distancia = "\nDistancia: " + direccionMapa.getDistancia() + "\n"
                .concat("Duración aproximada: " + direccionMapa.getDuracion());

        marker.setSnippet(marker.getSnippet().concat(distancia));

        markerActual = marker;

        ArrayList<LatLng> directionPoint = direccionMapa.getRuta();
        PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);

        for (LatLng puntoRuta : directionPoint) {
            rectLine.add(puntoRuta);
        }


        polyline = map.addPolyline(rectLine);

    }


    private ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            LocationService.MyBinder binder = (LocationService.MyBinder) service;
            locationService = binder.getService();

            //Sacamos la localización que tiene actualmente el servicio
            Location location = locationService.getCurrentLocation();

            handleLocation(location);
        }

        public void onServiceDisconnected(ComponentName arg0) {
            logManager.onServiceDisconnected();
        }

    };

    private void handleLocation(Location location) {
        // Add a marker of that location to the map
        LatLng latlongzoom = new LatLng(location.getLatitude(),
                location.getLongitude());
        String snippet = location.getLatitude() + "," + location.getLongitude();
        //La quitamos para que no vaya saliendo más de una vez
        if (posicionActual != null) {
            posicionActual.remove();
        }

        //Y ponemos la nueva
        posicionActual = map.addMarker(new MarkerOptions().position(latlongzoom));
        posicionActual.setSnippet(snippet);
        posicionActual.setTitle("Está usted aquí");


        // Center the map to the first marker
        if (currentLocation == null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latlongzoom).zoom(9).build();

            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            posicionActual = map.addMarker(new MarkerOptions().position(latlongzoom));
            posicionActual.setTitle("Está usted aquí");

        }
        currentLocation = location;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        locationClient.disconnect();
        unbindService(myConnection);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0,0);
        }
        return super.onOptionsItemSelected(item);
    }

    private class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            logManager.onBroadcastReceived();

            Location location = intent.getParcelableExtra(EcoApplication.LOCATION_KEY);

            handleLocation(location);
        }
    }
}