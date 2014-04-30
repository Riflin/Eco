package thinkbig.telefonica.eco;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.neosistec.utils.logmanager.LogManager;
import thinkbig.telefonica.eco.database.GestorHospitales;
import thinkbig.telefonica.eco.modelo.DireccionMapa;
import thinkbig.telefonica.eco.modelo.Hospital;

import java.util.ArrayList;


public class MapActivity extends FragmentActivity {
    private final static String TAG = MapActivity.class.getName();

    private final static int REQUEST_CODE = 112;

    private GoogleMap map;
    private LocationClient locationClient;
    private LocationCallback mLocationCallback = new LocationCallback();
    private Location currentLocation;
    private Marker posicionActual;
    private Polyline polyline;
    private Marker markerActual;
    private String distancia;

    private LogManager logManager = new LogManager(MapActivity.class.getName());

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Comprobamos que tenemos los servicios de Google instalados (y, si no los tiene, le dirigimos al market para bajarlos)
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (statusCode == ConnectionResult.SUCCESS) {
            Log.i(TAG, "Tiene Services instalados");
            init();
        } else {
            //Si puede instalarse los servicios lo enviamos allí.
            if (GooglePlayServicesUtil.isUserRecoverableError(statusCode)) {
                Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                        this, REQUEST_CODE);
                errorDialog.show();
            } else {
                // Handle unrecoverable error
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    init();
                    break;

            }
        }
    }

    /**
     * El locationClient luego habrá que ponerlo en un servicio aparte.
     */
    private void init() {

        if (locationClient == null) {
            locationClient = new LocationClient(this, mLocationCallback, mLocationCallback);
            Log.v(TAG, "Location Client connect");
            if (!(locationClient.isConnected() || locationClient.isConnecting())) {
                locationClient.connect();
            }
        }

        //Si no hay mapa lo creamos
        if (map == null) {
            FragmentManager myFragmentManager = getSupportFragmentManager();
            SupportMapFragment myMapFragment =
                    (SupportMapFragment) myFragmentManager.findFragmentById(R.id.mapa);
            map = myMapFragment.getMap();

            map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
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
            } else {
                //Ponemos un marcador en mi casa de Bullas
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(38.049694, -1.669925)).zoom(9).build();
                currentLocation = new Location("custom");
                currentLocation.setLatitude(38.049694);
                currentLocation.setLongitude(-1.669925);

                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                posicionActual = map.addMarker(new MarkerOptions().position(new LatLng(38.049694, -1.669925)));
                posicionActual.setTitle("Está usted aquí");
            }

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

    private class LocationCallback implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener,
            LocationListener {

        @Override
        public void onConnected(Bundle connectionHint) {
            Log.v(TAG, "Location Client connected");

            // Display last location
            Location location = locationClient.getLastLocation();
            if (location != null) {
                handleLocation(location);
            }

            // Request for location updates
            LocationRequest request = LocationRequest.create();
            request.setInterval(5000);
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationClient.requestLocationUpdates(request, mLocationCallback);
        }

        @Override
        public void onDisconnected() {
            Log.v(TAG, "Location Client disconnected by the system");
        }

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            Log.v(TAG, "Location Client connection failed");
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                Log.v(TAG, "onLocationChanged: location == null");
                return;
            }
            // Add a marker iff location has changed.
            if (currentLocation != null &&
                    currentLocation.getLatitude() == location.getLatitude() &&
                    currentLocation.getLongitude() == location.getLongitude()) {
                return;
            }

            handleLocation(location);
        }

        private void handleLocation(Location location) {
            // Update the mLocationStatus with the lat/lng of the location
            Log.v(TAG, "LocationChanged == @" +
                    location.getLatitude() + "," + location.getLongitude());

            // Add a marker of that location to the map
            LatLng latlongzoom = new LatLng(location.getLatitude(),
                    location.getLongitude());
            String snippet = location.getLatitude() + "," + location.getLongitude();
            //La quitamos para que no vaya saliendo más de una vez
            posicionActual.remove();

            //Y ponemos la nueva
            posicionActual = map.addMarker(
                    new MarkerOptions().position(latlongzoom));
            posicionActual.setSnippet(snippet);
            posicionActual.setTitle("Está usted aquí");


            // Center the map to the first marker
            if (currentLocation == null) {
                map.moveCamera(CameraUpdateFactory.
                        newCameraPosition(CameraPosition.fromLatLngZoom(
                                new LatLng(location.getLatitude(), location.getLongitude()),
                                (float) 16.0)));

            }
            currentLocation = location;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.disconnect();
    }
}