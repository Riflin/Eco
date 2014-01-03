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
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import thinkbig.telefonica.eco.modelo.DireccionMapa;

import java.util.ArrayList;


public class MapActivity extends FragmentActivity {
    private static final String TAG = MapActivity.class.getName();

    private GoogleMap map;
    private LocationClient locationClient;
    private LocationCallback mLocationCallback = new LocationCallback();
    private Location currentLocation;
    private Marker posicionActual;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Comprobamos que tenemos los servicios de Google instalados (y, si no los tiene, le dirigimos al market para bajarlos)
        int statusCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        if (statusCode == ConnectionResult.SUCCESS) {
            Log.i(TAG, "Tiene Services instalados");
            init();
        } else {
            //Si puede instalarse los servicios lo enviamos allí.
            if (GooglePlayServicesUtil.isUserRecoverableError(statusCode)) {
                Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                        this, 1002);
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
                case 1002:
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
            Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(38.10393, -1.86807)));
            marker.setTitle("Hospital de Caravaca");
            marker.setSnippet("Av de Prolongación Miguel de Espinosa, 1\n" +
                    "30400 Caravaca de la Cruz, Murcia\n" +
                    "968 70 91 00");
            BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.hospital_small);
            marker.setIcon(bm);

            marker = map.addMarker(new MarkerOptions().position(new LatLng(37.9331, -1.1637)));
            marker.setTitle("Hospital Virgen de la Arrixaca");
            marker.setSnippet("Murcia\n" +
                    "968 36 95 00");
            marker.setIcon(bm);

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
    }

    private void showDistance(Marker marker) {
        DireccionMapa direccionMapa = new DireccionMapa(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                marker.getPosition());

        marker.setSnippet(marker.getSnippet().concat("\nDistancia: " + direccionMapa.getDistancia()+"\n")
                .concat("Duración aproximada: " + direccionMapa.getDuracion()));
        ArrayList<LatLng> directionPoint = direccionMapa.getRuta();
        PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);

        for(int i = 0 ; i < directionPoint.size() ; i++) {
            rectLine.add(directionPoint.get(i));
        }

        map.addPolyline(rectLine);

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