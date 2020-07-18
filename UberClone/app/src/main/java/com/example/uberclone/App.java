package com.example.uberclone;

import android.app.Application;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.Parse;
import com.parse.ParseACL;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myappID")
                .clientKey("")   //Key Removed Cause of it's Sensitivity : API Key ALso Removed .
                .server("http://3.17.158.244/parse/")
                .build());

        ParseACL defaultACL = new ParseACL() ;
        defaultACL.setPublicReadAccess(true) ;
        defaultACL.setPublicWriteAccess(true) ;
        ParseACL.setDefaultACL(defaultACL , true) ;
    }
}
