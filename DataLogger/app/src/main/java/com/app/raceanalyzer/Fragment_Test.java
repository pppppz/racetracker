package com.app.raceanalyzer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.location.GpsStatus;

import org.w3c.dom.Text;

import java.util.Iterator;

public class Fragment_Test extends Fragment{
    private LocationManager mLocationManager;


    private TextView tvLocation;
    private TextView tvSatellite;

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

        mLocationManager = (LocationManager) super.getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.addGpsStatusListener(mGpsStatusListener);


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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        tvSatellite = (TextView) view.findViewById(R.id.tv_Satellite);

        return view;

    }



    private GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {
        private int mNumSatellites = -1;

        public void onGpsStatusChanged(int event) {
            if (event == GpsStatus.GPS_EVENT_STARTED) {
                Log.d("Fragment_Test", "GPS engine started");
            } else if (event == GpsStatus.GPS_EVENT_STOPPED) {
                Log.d("Fragment_Test", "GPS engine stopped");
            } else if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {
                GpsStatus status = mLocationManager.getGpsStatus(null);
                Log.d("Fragment_Test", "GPS engine first fix: " + status.getTimeToFirstFix() + "ms");
            } else {
                int satellites = 0;
                GpsStatus status = mLocationManager.getGpsStatus(null);
                Iterable<GpsSatellite> list = status.getSatellites();
                Iterator<GpsSatellite> i = list.iterator();
                while (i.hasNext()) {
                    satellites ++;
                    i.next();
                }

                if (satellites != mNumSatellites) {
                    mNumSatellites = satellites;
                    Log.d("Fragment_Test", "GPS satellites: " + satellites);
                    tvSatellite.setText("GPS satellites: " + satellites);
                }
            }
        }
    };

}
