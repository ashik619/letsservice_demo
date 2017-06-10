package com.ashik619.letssevicedemo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ashik619.letssevicedemo.R;
import com.ashik619.letssevicedemo.helper.ConvertTime;
import com.ashik619.letssevicedemo.models.LocationObject;

import java.util.Calendar;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by ashik619 on 10-06-2017.
 */
public class LocationListAdapter extends RealmBaseAdapter<LocationObject> implements ListAdapter {
    RealmResults<LocationObject> realmResults;
    Context context;
    public LocationListAdapter(Context context, RealmResults<LocationObject> realmResults){
        super(realmResults);
        this.realmResults = realmResults;
        this.context = context;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.loc_list_item, viewGroup, false);
        }
        LocationObject locationObject = realmResults.get(i);
        TextView lat = (TextView) view.findViewById(R.id.lat);
        TextView lng = (TextView) view.findViewById(R.id.lng);
        TextView time = (TextView) view.findViewById(R.id.time);
        lat.setText("Latitude : "+String.valueOf(locationObject.getLat()));
        lng.setText("Longitude : "+String.valueOf(locationObject.getLng()));
        time.setText("Time : "+(new ConvertTime(String.valueOf(locationObject.getTime())).getSureTime(1)));
        return view;
    }
}