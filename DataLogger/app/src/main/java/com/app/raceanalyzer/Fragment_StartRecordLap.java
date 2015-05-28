package com.app.raceanalyzer;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
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

import com.app.raceanalyzer.Database.HeadLapDatabase;
import com.app.raceanalyzer.SensorListener.AccelerometerListener;
import com.app.raceanalyzer.SensorListener.GPS_Listener;
import com.app.raceanalyzer.SensorListener.LocationListener;
import com.google.android.gms.maps.model.LatLng;

public class Fragment_StartRecordLap extends Fragment {

    public static TextView tvSatellite_InView, tvSatellite_InUse, tvSpeed;
    //accelorometer
    public static double axis_x, axis_y, axis_z, velocity;
    public static long updatedTime = 0L;
    //location
    protected LocationManager locationManager;
    //time
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long bestLapTime = 0L;
    long startTime = 0L;
    LatLng StartLocation;
    private Location location;
    private Button startButton;
    //   public static double latitude;
    //  public static double longitude;
    private Handler customHandler = new Handler();


    //parse
    private LocationListener locationListener;
    private View view;
    private TextView timerValue;
    private SensorManager sensorMgr;
    private GPS_Listener gpsListener;
    //GPS
    private Criteria criteria;
    private String provider;
    private long record_id;
    private AccelerometerListener accelerometerListener;
    private HeadLapDatabase headLap;


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


    @Override
    public void onResume() {
        super.onResume();

        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (startTime == 0L) {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    startButton.setBackgroundColor(Color.parseColor("#F44336"));
                    startButton.setText("Stop");
                } else {
                    /*timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);*/
                    //for remove thread count time
                    customHandler.removeCallbacks(updateTimerThread);

                    //            String time = null;
                    // switch Fragment to start record lap
                    Bundle bundle = new Bundle(); //  bundle function is management resource , state
                    bundle.putLong("timelap", updatedTime);
                    bundle.putLong("record_id", record_id);
                    Fragment fragment = new Fragment_SaveToCloud();
                    fragment.setArguments(bundle);
                    new switchFragment(fragment, getFragmentManager()).doSwitch();

                }
            }
        });

        setAboutLocation();
        setAboutAccelerometer();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        //close accelerometer & GPS listener (for save battery)
        sensorMgr.unregisterListener(accelerometerListener);
        locationManager.removeGpsStatusListener(gpsListener);
        locationManager.removeUpdates(locationListener);
        long headlap_keyid = LocationListener.headLap_IDKey;

        Log.e("Start record : ", headlap_keyid + " " + updatedTime);
        headLap.updateRecord(headlap_keyid, updatedTime);
        updatedTime = 0L;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // declare xml to java
        view = inflater.inflate(R.layout.fragment_start_record_lap, container, false);
        timerValue = (TextView) view.findViewById(R.id.timerValue);
        startButton = (Button) view.findViewById(R.id.startButton);
        tvSpeed = (TextView) view.findViewById(R.id.tv_Speed);
        tvSatellite_InView = (TextView) view.findViewById(R.id.tv_satellite_in_view);
        tvSatellite_InUse = (TextView) view.findViewById(R.id.tv_satellite_in_use);

        headLap = new HeadLapDatabase(getActivity());
        // get location & round id from fragment_chooseStartPoint
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            // then you have arguments
            StartLocation = getArguments().getParcelable("location");
            record_id = getArguments().getLong("record_id");
            Log.e("location start ", StartLocation.toString() + "Record id : " + record_id);
        } else {
            Log.e("location & round", " not set");
        }
        return view;
    }

    public void setAboutAccelerometer() {


        sensorMgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorAccelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerListener = new AccelerometerListener();
        sensorMgr.registerListener(accelerometerListener, sensorAccelerometer, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
    }

    public void setAboutLocation() {

        // Define the criteria for GPS
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);

        // set Provider for GPS
        locationManager = (LocationManager) super.getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);  // the last known location of this provider
        locationListener = new LocationListener(StartLocation, record_id, getActivity());
        gpsListener = new GPS_Listener(locationManager);
        locationManager.addGpsStatusListener(gpsListener);


        // location updates: at least 1 meter and 200millsecs change
        int MIN_SEC_WHEN_CHANGE = 200;
        int MIN_METER_CHANGE = 1;
        locationManager.requestLocationUpdates(provider, MIN_SEC_WHEN_CHANGE, MIN_METER_CHANGE, locationListener);
    }
}