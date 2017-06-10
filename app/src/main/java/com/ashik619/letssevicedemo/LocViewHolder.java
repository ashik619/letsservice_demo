package com.ashik619.letssevicedemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashik619.letssevicedemo.helper.ConvertTime;
import com.ashik619.letssevicedemo.models.LocationObject;

import java.util.ArrayList;

/**
 * Created by ashik619 on 11-06-2017.
 */
public class LocViewHolder extends RecyclerView.ViewHolder {
    public TextView lat;
    public TextView lng;
    public TextView time;
    Context context;

    public LocViewHolder(View itemView, Context context )
    {
        super(itemView);
        this.context = context;
        lat = (TextView) itemView.findViewById(R.id.lat);
        lng = (TextView) itemView.findViewById(R.id.lng);
        time = (TextView) itemView.findViewById(R.id.time);

    }


}