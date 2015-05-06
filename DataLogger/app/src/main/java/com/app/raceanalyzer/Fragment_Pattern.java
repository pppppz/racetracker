package com.app.raceanalyzer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

import java.util.Iterator;

public class Fragment_Pattern extends Fragment{

    private TextView tvLocation;

    private View view;
    private Activity a;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...


            }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //  mURL = savedInstanceState.getString("currentURL", "");
            //Log.e("frag home", mURL);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //  outState.putString("currentURL", mURL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;

    }

}
