package com.example.readerhabit

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import io.realm.Realm
import io.realm.RealmConfiguration



class MyApplication : Application(){
    override fun onCreate(){
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config);

    }
}