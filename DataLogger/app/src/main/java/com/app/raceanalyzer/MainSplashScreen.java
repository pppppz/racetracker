package com.app.raceanalyzer;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainSplashScreen extends ActionBarActivity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(com.app.raceanalyzer.R.layout.main_splash_screen);
        getSupportActionBar().hide();

// METHOD 1

        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(1000); //set at 1 sec

                    // After 5 seconds redirect to another intent
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);

                    //Remove activity
                    finish();

                } catch (Exception e) {

                    Log.e("MainSplash", e.getLocalizedMessage());

                }
            }
        };

        // start thread
        background.start();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}