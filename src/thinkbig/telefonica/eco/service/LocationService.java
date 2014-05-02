package thinkbig.telefonica.eco.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.neosistec.utils.logmanager.LogManager;
import thinkbig.telefonica.eco.EcoApplication;

/**
 * Se encarga de ir recogiendo puntos del servidor gps
 */
public class LocationService extends Service {

    private final LogManager logManager = new LogManager(LocationService.class.getName());

    private final MyBinder myBinder = new MyBinder();
    private LocationClient locationClient;
    private LocationCallback mLocationCallback = new LocationCallback();
    private Location currentLocation;

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {

        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (locationClient == null) {
            locationClient = new LocationClient(this, mLocationCallback, mLocationCallback);
            logManager.verbose("Location Client connect");
            if (!(locationClient.isConnected() || locationClient.isConnecting())) {
                locationClient.connect();
            }
        }
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    private class LocationCallback implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener,
            LocationListener {

        @Override
        public void onConnected(Bundle connectionHint) {
            logManager.verbose("Location Client connected");

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
            logManager.verbose("Location Client disconnected by the system");
        }

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            logManager.verbose("Location Client connection failed");
        }

        @Override
        public void onLocationChanged(Location location) {

            logManager.verbose("Location changed: " + location);
            if (location == null) {
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

            currentLocation = location;
            Intent i = new Intent(EcoApplication.LOCATION_CHANGED);
            i.putExtra(EcoApplication.LOCATION_KEY, location);
            LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(i);
        }
    }
}
