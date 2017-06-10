package com.ashik619.letssevicedemo.services;

import android.app.Service;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.ashik619.letssevicedemo.models.LocationObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import io.realm.Realm;

/**
 * Created by ashik619 on 10-06-2017.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 10;
    private static final String TAG = LocationService.class.getSimpleName();

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;




    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG,"building");
        createLocationRequest();

        buildGoogleApiClient();

        mGoogleApiClient.connect();


        return START_STICKY;

    }


    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
     void buildGoogleApiClient() {
        Log.e(TAG,"building");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(INTERVAL);

        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        Log.e(TAG,"starting loc updates");
        if(mGoogleApiClient.isConnected()) {
            try {

                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);

            } catch (SecurityException ex) {
                ex.printStackTrace();

            }
        }
    }


    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */


    /**
     * Send broadcast using LocalBroadcastManager to update UI in activity
     *
     * @param sbLocationData
     */

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onDestroy() {

        Log.e(TAG,"stoping loc updates");

        stopLocationUpdates();

        mGoogleApiClient.disconnect();




        super.onDestroy();
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException {
        Log.e(TAG, "Connected to GoogleApiClient");


      //  if (mCurrentLocation == null) {
         //   mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
      //  }
        Log.e(TAG,"starting loc updates");
        startLocationUpdates();

    }

    /**
     * Callback that fires when the location changes.
     */
    Realm myRealm;

    @Override
    public void onLocationChanged(final Location location) {
        Log.e(TAG,"loc"+location.getLongitude());
        mCurrentLocation = location;
        Realm.init(this);
        myRealm = Realm.getDefaultInstance();
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.e(TAG,""+location.getLongitude());
                LocationObject locationObject = realm.createObject(LocationObject.class);
                locationObject.setLat(location.getLatitude());
                locationObject.setLng(location.getLongitude());
                locationObject.setTime(System.currentTimeMillis());
            }
        });

    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = ");

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
