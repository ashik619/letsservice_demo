package com.ashik619.letssevicedemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ashik619.letssevicedemo.LocViewHolder;
import com.ashik619.letssevicedemo.R;
import com.ashik619.letssevicedemo.helper.ConvertTime;
import com.ashik619.letssevicedemo.models.LocationObject;

import java.util.ArrayList;
import java.util.Calendar;


import io.realm.RealmResults;

/**
 * Created by ashik619 on 10-06-2017.
 */
public class LocationListAdapter  extends RecyclerView.Adapter<LocViewHolder>{
    ArrayList<LocationObject> locationObjects;
    Context context;
    public LocationListAdapter(Context context, ArrayList<LocationObject> locationObjects){
        this.locationObjects = locationObjects;
        this.context = context;
    }


    @Override
    public LocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.loc_list_item, null);
        LocViewHolder rcv = new LocViewHolder(layoutView,context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(LocViewHolder holder, int position) {

        holder.lat.setText("Lat : "+String.valueOf(locationObjects.get(position).getLat()));
        holder.lng.setText("Long: "+String.valueOf(locationObjects.get(position).getLng()));
        holder.time.setText("Time : "+(new ConvertTime(String.valueOf(locationObjects.get(position).getTime())).getSureTime(1)));

    }

    @Override
    public int getItemCount() {
        return this.locationObjects.size();
    }
}