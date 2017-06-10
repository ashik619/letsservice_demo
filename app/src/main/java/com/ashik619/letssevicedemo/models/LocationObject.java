package com.ashik619.letssevicedemo.models;

import io.realm.RealmObject;

/**
 * Created by ashik619 on 10-06-2017.
 */
public class LocationObject extends RealmObject {
    public Double lat;
    public Double lng;
    public Long time;

    public Double getLat() {
        return lat;
    }
    public Double getLng() {
        return lng;
    }

    public Long getTime() {
        return time;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
