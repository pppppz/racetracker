package com.app.raceanalyzer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

import org.w3c.dom.Text;

import java.util.Iterator;

public class Fragment_Home extends Fragment {

    private TextView tvLocation;
    private TextView tvSpeed;
    private TextView tvSatellite_inview , tvSatellite_inuse;

    private View view;
    private Activity a;
    private Button startButton;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    protected LocationManager locationManager;
    MyLocationListener myLocationListener;

    private float latitude;
    private float longitude;
    private Criteria criteria;
    private String provider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (startTime == 0L) {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                } else {
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                }
            }
        });

        // Get the location manager
        locationManager = (LocationManager) super.getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = locationManager.getBestProvider(criteria, false);

        // the last known location of this provider
        Location location = locationManager.getLastKnownLocation(provider);

        locationManager.addGpsStatusListener(mGpsStatusListener);
        myLocationListener = new MyLocationListener();

        /*
        if (location != null) {
            myLocationListener.onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }*/

        // location updates: at least 1 meter and 200millsecs change
       locationManager.requestLocationUpdates(provider , 200 , 1 , myLocationListener);
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
        timerValue = (TextView) view.findViewById(R.id.timerValue);
        startButton = (Button) view.findViewById(R.id.startButton);
        tvSpeed = (TextView) view.findViewById(R.id.tv_Speed);
        tvSatellite_inview = (TextView) view.findViewById(R.id.tv_satellite_in_view);
        tvSatellite_inuse = (TextView) view.findViewById(R.id.tv_satellite_in_use);

        return view;

    }


    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };

    private GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {



        private int mNumSatellites = -1;

        public void onGpsStatusChanged(int event) {


            String strGpsStats = "";

            GpsStatus gpsStatus = locationManager.getGpsStatus(null);
            if (gpsStatus != null) {
                Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
                Iterator<GpsSatellite> sat = satellites.iterator();
                int i = 0;
              /*  while (sat.hasNext()) {
                    GpsSatellite satellite = sat.next();
                    strGpsStats += (i++) + ": " + satellite.getPrn() + "," + satellite.usedInFix() + "," + satellite.getSnr() + "," + satellite.getAzimuth() + "," + satellite.getElevation() + "\n\n";
                }

                // Toast.makeText(getBaseContext(), strGpsStats, Toast.LENGTH_LONG).show();
                Log.i("T", "GPS STATUS HAS CHANGED:" + strGpsStats);
           //     tvSatellite.setText("GPS satellites: " + strGpsStats);
                tvSatellite.setText(strGpsStats);
                */
                int iTempCountInView = 0;
                int iTempCountInUse = 0;

                if (satellites != null) {
                    for (GpsSatellite gpsSatellite : satellites) {
                        iTempCountInView++;
                        if (gpsSatellite.usedInFix()) {
                            iTempCountInUse++;
                        }
                    }
                }
                tvSatellite_inview.setText("In view : " + iTempCountInView);
                tvSatellite_inuse.setText("In use : " + iTempCountInUse);

            }
        }
    };


    private class MyLocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
/*
            // Initialize the location fields

            latitude.setText("Latitude: "+String.valueOf(location.getLatitude()));

            longitude.setText("Longitude: "+String.valueOf(location.getLongitude()));

            provText.setText(provider + " provider has been selected.");


            Toast.makeText(MainActivity.this,  "Location changed!",

                    Toast.LENGTH_SHORT).show();
*/
            location.getLatitude();
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
            tvLocation.setText("latitude : " + latitude + " Longitude : " + longitude);
            tvSpeed.setText("Current speed:" + location.getSpeed());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            Toast.makeText( getActivity(), provider + "'s status changed to "+status +"!",

                    Toast.LENGTH_SHORT).show();

            Log.e("test" , provider + "'s status changed to "+status +"!");

        }

        @Override
        public void onProviderEnabled(String provider) {

            Toast.makeText(getActivity(), "Provider " + provider + " enabled!",

                    Toast.LENGTH_SHORT).show();

        }
        @Override
        public void onProviderDisabled(String provider) {

            Toast.makeText(getActivity(), "Provider " + provider + " disabled!",

                    Toast.LENGTH_SHORT).show();

        }
    }


}

/*
    @Override
    public void onLocationChanged(Location location) {

        location.getLatitude();
        latitude = (float) location.getLatitude();
        longitude = (float) location.getLongitude();
        tvLocation.setText("latitude : " + latitude + " Longitude : " + longitude);
        tvSpeed.setText("Current speed:" + location.getSpeed());
    }*/

/*
    private GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {
        private int mNumSatellites = -1;

        public void onGpsStatusChanged(int event) {


            String strGpsStats = "";

            GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
            if (gpsStatus != null) {
                Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
                Iterator<GpsSatellite> sat = satellites.iterator();
                int i = 0;
                while (sat.hasNext()) {
                    GpsSatellite satellite = sat.next();
                    strGpsStats += (i++) + ": " + satellite.getPrn() + "," + satellite.usedInFix() + "," + satellite.getSnr() + "," + satellite.getAzimuth() + "," + satellite.getElevation() + "\n\n";
                }

                // Toast.makeText(getBaseContext(), strGpsStats, Toast.LENGTH_LONG).show();
                Log.i("T", "GPS STATUS HAS CHANGED:" + strGpsStats);
                tvSatellite.setText("GPS satellites: " + strGpsStats);

            }
        }
    };*/







/*
    public void onGpsStatusChanged(int arg0) {

        String strGpsStats = "";

        GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
        if (gpsStatus != null) {
            Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
            Iterator<GpsSatellite> sat = satellites.iterator();
            int i = 0;
            while (sat.hasNext()) {
                GpsSatellite satellite = sat.next();

                strGpsStats += (i++) + ": " + satellite.getPrn() + "," + satellite.usedInFix() + "," + satellite.getSnr() + "," + satellite.getAzimuth() + "," + satellite.getElevation() + "\n\n";
            }

            // Toast.makeText(getBaseContext(), strGpsStats, Toast.LENGTH_LONG).show();
            Log.i("T", "GPS STATUS HAS CHANGED:" + strGpsStats);
        }
    }*/



     /*
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
                    satellites++;
                    i.next();
                }

                if (satellites != mNumSatellites) {
                    mNumSatellites = satellites;
                    Log.d("Fragment_Test", "GPS satellites: " + satellites);
                    tvSatellite.setText("GPS satellites: " + satellites);
                }
            } */
