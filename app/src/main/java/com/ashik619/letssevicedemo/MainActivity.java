package com.ashik619.letssevicedemo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.listView)
    ListView listView;
    LocationListAdapter listAdapter;
    Realm myRealm;
    GoogleMap mMap;
    Marker orginMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        startLocationService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
    void deleteAll(){
        RealmResults<LocationObject> realmResults = myRealm.where(LocationObject.class).findAll();

        if (realmResults.size() != 0) {
            Log.e("MAIN","deleting");

            myRealm.beginTransaction();
            realmResults.deleteAllFromRealm();
            myRealm.commitTransaction();
        }
    }

    void startLocationService() {
        Intent service = new Intent(MainActivity.this, LocationService.class);
        startService(service);
    }
    boolean isFirst = true;
    void populateListView(Realm realm){
        RealmResults<LocationObject> list = realm.where(LocationObject.class).findAll();
        LocationObject lastLoc = list.last();
        if(isFirst) {
            isFirst = false;
            addMarker(lastLoc.getLat(), lastLoc.getLng());
        }else updateMarker(lastLoc.getLat(), lastLoc.getLng());
        Log.e("MAIN","org size"+list.size());
        //list = list.sort("time", Sort.DESCENDING);
        if (list.size()>100) {
            Log.e("MAIN","sixe grate than 100");
            list.subList(list.size()-100, list.size());
        }
        Log.e("MAIN","after sort"+list.size());
        listAdapter = new LocationListAdapter(MainActivity.this,list);
        listView.setAdapter(listAdapter);
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
}
