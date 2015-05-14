package com.app.raceanalyzer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.Toast;

import com.app.raceanalyzer.Database.createLapHeaderDB;
import com.app.raceanalyzer.Database.createLapLocationChangeDB;
import com.app.raceanalyzer.Database.createRecordDB;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseUser;

public class Fragment_StartRecordLap extends Fragment {

    //location
    protected LocationManager locationManager;
    //time
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    long bestLapTime = 0L;
    long startTime = 0L;
    LatLng StartLocation;
    //connect DB
    private createLapLocationChangeDB lapDB;
    private createRecordDB recordDB;
    private createLapHeaderDB lapHeaderDB;
    private Handler customHandler = new Handler();
    //parse
    private ParseUser parseUSER;
    private MyLocationListener myLocationListener;
    private TextView tvSatellite_InView, tvSatellite_InUse, tvSpeed;
    private View view;
    private Button startButton;
    private TextView timerValue;
    private SensorManager sensorMgr;
    private float latitude, longitude;


    //GPS
    private Criteria criteria;
    private String provider;
    private float bestLapSpeed;

    //axis
    private double axis_x, axis_y, axis_z, velocity;
    SensorEventListener AccelerometerSensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            axis_x = event.values[0];
            axis_y = event.values[1];
            axis_z = event.values[2];
        }
    };
    private long round_id;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int sec = (int) (updatedTime / 1000);
            int min = sec / 60;
            sec = sec % 60;
            int milliseconds = (int) (updatedTime % 1000);

            timerValue.setText("" + min + ":"
                    + String.format("%02d", sec) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };
    private GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {

        public void onGpsStatusChanged(int event) {


            // String strGpsStats = "";
            GpsStatus gpsStatus = locationManager.getGpsStatus(null);
            if (gpsStatus != null) {
                Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
                //  Iterator<GpsSatellite> sat = satellites.iterator();

                int CountInView = 0;
                int CountInUse = 0;

                if (satellites != null) {
                    for (GpsSatellite gpsSatellite : satellites) {
                        CountInView++;
                        if (gpsSatellite.usedInFix()) {
                            CountInUse++;
                        }
                    }
                }
                tvSatellite_InView.setText("In view : " + CountInView);
                tvSatellite_InUse.setText("In use : " + CountInUse);

            }
        }
    };

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
        //location = locationManager.getLastKnownLocation(provider);

        myLocationListener = new MyLocationListener();
        locationManager.addGpsStatusListener(mGpsStatusListener);

        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(provider, 200, 1, myLocationListener);

        //accelerometer
        sensorMgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMgr.registerListener(AccelerometerSensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);

        Log.e(Fragment_StartRecordLap.class.getName(), String.valueOf(round_id));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /** declare xml to java*/
        view = inflater.inflate(R.layout.fragment_record_lap, container, false);
        timerValue = (TextView) view.findViewById(R.id.timerValue);
        startButton = (Button) view.findViewById(R.id.startButton);
        tvSpeed = (TextView) view.findViewById(R.id.tv_Speed);
        tvSatellite_InView = (TextView) view.findViewById(R.id.tv_satellite_in_view);
        tvSatellite_InUse = (TextView) view.findViewById(R.id.tv_satellite_in_use);

        /** get location & round id from fragment_chooseStartPoint */
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            // then you have arguments
            StartLocation = getArguments().getParcelable("location");
            round_id = getArguments().getLong("round_id");

            Log.e("location start ", StartLocation.toString() + "Round id : " + round_id);
        } else {
            // no arguments supplied...
            Log.e("location & round", " not set");
        }
        //record to createRecordDB
        lapDB = new createLapLocationChangeDB(getActivity());
        return view;
    }

    protected void addDataToSQLite(double axis_x, double axis_y, double axis_z, double velocity, long round_id, double lat, double lng) {
        parseUSER = ParseUser.getCurrentUser();
        lapDB.addNewLocationChange(parseUSER.getUsername(), axis_x, axis_y, axis_z, velocity, round_id, lat, lng);

    }

    private class MyLocationListener implements android.location.LocationListener {

        /**
         * if location listener .. accelerometer add data into sqlite
         */

        @Override
        public void onLocationChanged(Location location) {

            //get location
            location.getLatitude();
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();

            //speed
            velocity = location.getSpeed();

            if (latitude == StartLocation.latitude && longitude == StartLocation.longitude) {

                if (updatedTime < bestLapTime) {
                    //set best lap
                    CharSequence time = timerValue.getText();
                    Log.e("best lap ", (String) time);

                    //update record
                    recordDB.updateRecord(parseUSER.getUsername(), bestLapTime, round_id);
                }

                //clear last lap
                updatedTime = 0L;

                //start new lap
                lapDB = new createLapLocationChangeDB(getActivity());

            }

            if (bestLapSpeed > location.getSpeed()) {
                bestLapSpeed = location.getSpeed();
            }

            Log.e("location now : ", "latitude : " + latitude + " Longitude : " + longitude);
            tvSpeed.setText("Current speed:" + velocity);
            addDataToSQLite(axis_x, axis_y, axis_z, velocity, round_id, latitude, longitude);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            Toast.makeText(getActivity(), provider + "'s status changed to " + status + "!",

                    Toast.LENGTH_SHORT).show();

            Log.e("test", provider + "'s status changed to " + status + "!");

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