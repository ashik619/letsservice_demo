package com.ashik619.letssevicedemo;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ashik619.letssevicedemo.adapters.LocationListAdapter;
import com.ashik619.letssevicedemo.models.LocationObject;
import com.ashik619.letssevicedemo.services.LocationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.listView)
    RecyclerView listView;
    LocationListAdapter listAdapter;
    Realm myRealm;
    GoogleMap mMap;
    Marker orginMarker;
    private static int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {  android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS };
    LocationManager locationManager;
    boolean isPerGranted = false;
    boolean isLocOn = false;
    RealmResults<LocationObject> list;
    ArrayList<LocationObject> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        listView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Realm.init(this);
        myRealm = Realm.getDefaultInstance();
        deleteAll();

        myRealm.addChangeListener(new RealmChangeListener<Realm>() {

            @Override
            public void onChange(Realm realm) {
                Log.e("MAIN","change");
                populateListView(realm);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        } else {
            permissionGranted();
        }

    }
    void populateList(Realm realm){

        RealmResults realmResults = realm.where(LocationObject.class).findAll();
        if (realmResults.size()>100) {
            Log.e("MAIN","sixe grate than 100");
            List<LocationObject> subList = realmResults.subList((realmResults.size()-100), realmResults.size());
            arrayList = new ArrayList<>(subList);
        } else arrayList = new ArrayList<>(realmResults);
        listAdapter = new LocationListAdapter(MainActivity.this,arrayList);
        listView.setAdapter(listAdapter);
    }
    void showTurnOnLoacationDialog() {
        Toast.makeText(MainActivity.this, "Turn on Location",
                Toast.LENGTH_LONG).show();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!isLocationEnabled()) {
            if(!isLocOn) {
                isLocOn = true;
                showTurnOnLoacationDialog();
            }
        } else {
            if(!isLocOn) {
                isLocOn = true;
                populateList(myRealm);
                locationIsOn();
            }
        }
    }

    void deleteAll(){
        RealmResults<LocationObject> realmResults = myRealm.where(LocationObject.class).findAll();

        if (realmResults.size() != 0) {
            Log.e("MAIN","deleting");
            if(realmResults.size()>220) {
                realmResults.subList(0, (realmResults.size() - 101));

                myRealm.beginTransaction();
                realmResults.deleteAllFromRealm();
                myRealm.commitTransaction();
            }
        }
    }

    void startLocationService() {
        Intent service = new Intent(MainActivity.this, LocationService.class);
        startService(service);
    }
    boolean isFirst = true;
    void populateListView(Realm realm){

        list = realm.where(LocationObject.class).findAll();
        if(list.size()>0) {
            LocationObject lastLoc = list.last();
            if (isFirst) {
                isFirst = false;
                addMarker(lastLoc.getLat(), lastLoc.getLng());
            } else updateMarker(lastLoc.getLat(), lastLoc.getLng());
        }
        Log.e("MAIN","org size"+list.size());
        populateList(realm);

    }
    boolean isMapReady = false;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        isMapReady = true;
    }
    MarkerOptions markerOptions;
    void addMarker(Double lat,Double lng){

        if(isMapReady) {
            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(lat,lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            markerOptions = new MarkerOptions().position(latLng).title("Current Location");
            orginMarker = mMap.addMarker(markerOptions);

        }
    }
    void updateMarker(Double lat,Double lng){
        LatLng latLng = new LatLng(lat,lng);
        orginMarker.setPosition(latLng);
    }
    void checkPermissions(){

        if(!hasPermissions(MainActivity.this, PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        } else {
            permissionGranted();
        }

    }
    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == PERMISSION_ALL){

            //If permission is granted
            boolean flag = false;
            for(int grantResult : grantResults){
                if(grantResult != PackageManager.PERMISSION_GRANTED){
                    //  System.out.println("permission not granted");
                    requestPermissionAgain();
                    flag = false;
                } else {
                    // System.out.println("permission granted");
                    flag = true;
                }
            }
            if(flag){
                if(!isPerGranted) {
                    Log.e("MAIn","loop1");
                    isPerGranted = true;
                    permissionGranted();
                }
            }

        }
    }


    void requestPermissionAgain(){
        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
    }
    void permissionGranted(){
        if (!isLocationEnabled()) {
            showTurnOnLoacationDialog();
        } else {
            locationIsOn();
        }

    }
    void locationIsOn(){
        startLocationService();
    }



    public boolean isLocationEnabled(){
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        }else{
            return false;
        }
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {

            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        params.height += listView.getDividerHeight() * listAdapter.getCount();

        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
