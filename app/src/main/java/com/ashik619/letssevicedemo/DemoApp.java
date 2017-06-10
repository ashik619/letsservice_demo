package com.ashik619.letssevicedemo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ashik619 on 10-06-2017.
 */
public class DemoApp extends Application {

    public RealmConfiguration feedRealmConfig;
    @Override
    public void onCreate() {
        super.onCreate();
        /*feedRealmConfig =  new RealmConfiguration.Builder(this)
                .name("feed")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();*/
    }
}
