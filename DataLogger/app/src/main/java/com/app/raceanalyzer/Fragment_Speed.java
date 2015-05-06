package com.app.raceanalyzer;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Fragment_Speed extends Fragment {

    double dLatitude, dAltitude, dLongitude, dAccuracy, dSpeed, dSats;
    float fAccuracy, fSpeed;
    long lSatTime;     // satellite time
    String szSignalSource, szAltitude, szAccuracy, szSpeed;
    public String szSatellitesInView;
    public String szSatellitesInUse;
    public static String szSatTime;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    private static final long MIN_TIME_BW_UPDATES = 1000; //1 sec

    protected GpsListener gpsListener = new GpsListener();

    Location location; // location
    private TextView tv_speed;
    private TextView tv_satelite;
    private LocationManager locationManager;
    private LocationListener locationlistener;
    double mySpeed, maxSpeed;
    private final String Speed = null;



    private RelativeLayout RL_Layout;
    private FragmentActivity faActivity;

    private GpsStatus mGpsStatus;
    boolean isGPSEnabled = false; // flag for GPS satellite status
    boolean isNetworkEnabled = false; // flag for cellular network status
    boolean canGetLocation = false; // flag for either cellular or satellite status

    public Fragment_Speed(){

    }

  /*  public Fragment_Speed(Context context) {
        this.mContext = context;
        getLocation();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //force use activity for fragment
        faActivity = super.getActivity();

        //UI
        RL_Layout = (RelativeLayout) inflater.inflate(com.app.raceanalyzer.R.layout.fragment_speed, container, false);
        tv_satelite = (TextView) RL_Layout.findViewById(com.app.raceanalyzer.R.id.tv_Satellite);
        tv_speed = (TextView) RL_Layout.findViewById(com.app.raceanalyzer.R.id.tv_Speed);

        //ivImage = (ImageView) llLayout.findViewById(R.id.image);
        //ivImage.setOnClickListener(onClickImage);

        //Speed
        maxSpeed = mySpeed = 0;
        locationManager = (LocationManager) faActivity.getSystemService(Context.LOCATION_SERVICE);
        locationlistener = new SpeedActionListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);




        return RL_Layout;
    }


    class GpsListener implements GpsStatus.Listener{
        @Override
        public void onGpsStatusChanged(int event) {
        }
    }
    /*
    public Location getLocation() {
        try {
          //  locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);// getting GPS satellite status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);// getting cellular network status
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {//GPS is enabled, getting lat/long via cellular towers
                    locationManager.addGpsStatusListener(gpsListener);//inserted new
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    Log.d("Cell tower", "Cell tower");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            szAltitude = " NA (using cell towers)";
                            szSatellitesInView = " NA (using cell towers)";
                            szSatellitesInUse = " NA (using cell towers)";
                        }
                    }
                }
                if (isGPSEnabled) {//GPS is enabled, gettoing lat/long via satellite
                    if (location == null) {
                        locationManager.addGpsStatusListener(gpsListener);//inserted new
                        locationManager.getGpsStatus(null);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                dAltitude = location.getAltitude();
                                szAltitude = String.valueOf(dAltitude);
                                /**************************************************************
                                 * Provides a count of satellites in view, and satellites in use
                                 **************************************************************/
                    /*            mGpsStatus = locationManager.getGpsStatus(mGpsStatus);
                                Iterable<GpsSatellite> satellites = mGpsStatus.getSatellites();
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
                                szSatellitesInView = String.valueOf(iTempCountInView);
                                szSatellitesInUse = String.valueOf(iTempCountInUse);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }*/




    class SpeedActionListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                if (location.hasSpeed()) {
                    double mySpeed = location.getSpeed();
                    tv_speed.setText("\nCurrent speed: " + mySpeed + " km/h, Max speed: " +
                            maxSpeed + " km/h");

                    Log.e("Speed ", " = " + mySpeed);
                    Log.e("Speed " , " = " + maxSpeed);
                }
            }
        }


        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle
                extras) {
         int satellites = 0;
        int satellitesInFix = 0;
        int timetofix = locationManager.getGpsStatus(null).getTimeToFirstFix();
        Log.e("GPS Count ", "Time to first fix = " + String.valueOf(timetofix));
        for (GpsSatellite sat : locationManager.getGpsStatus(null).getSatellites()) {
            if (sat.usedInFix()) {
                satellitesInFix++;
            }
            satellites++;
        }
        String text = "GPS" + String.valueOf(satellites) + " Used In Last Fix  " + satellitesInFix;
        Log.e("GPS", String.valueOf(satellites) + " Used In Last Fix (" + satellitesInFix + ")");

        tv_satelite.setText(text);

        }
    }
}





