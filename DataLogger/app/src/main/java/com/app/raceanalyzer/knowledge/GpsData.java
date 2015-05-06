package com.app.raceanalyzer.knowledge;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

public class GpsData extends Service implements LocationListener {

boolean isGPSEnabled = false; // flag for GPS satellite status
boolean isNetworkEnabled = false; // flag for cellular network status
boolean canGetLocation = false; // flag for either cellular or satellite status

private GpsStatus mGpsStatus;
private final Context mContext;
protected LocationManager locationManager;
protected GpsListener gpsListener = new GpsListener();

Location location; // location
double dLatitude, dAltitude, dLongitude, dAccuracy, dSpeed, dSats; 
float fAccuracy, fSpeed;
long lSatTime;     // satellite time
String szSignalSource, szAltitude, szAccuracy, szSpeed;

public String szSatellitesInUse, szSatellitesInView;
public static String szSatTime;
private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
private static final long MIN_TIME_BW_UPDATES = 1000; //1 second

public GpsData(Context context) {
    this.mContext = context;
    getLocation();  
}

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class GpsListener implements GpsStatus.Listener{
    @Override
    public void onGpsStatusChanged(int event) {

    }
}   
public Location getLocation() {
    try {
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);// getting GPS satellite status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);// getting cellular network status
        if (!isGPSEnabled && !isNetworkEnabled) {
        } else {
            this.canGetLocation = true;
            if (isNetworkEnabled) {//GPS is enabled, getting lat/long via cellular towers
                locationManager.addGpsStatusListener(gpsListener);//inserted new
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) { 
                            dAltitude = location.getAltitude();
                            szAltitude = String.valueOf(dAltitude);
                            /**************************************************************
                             * Provides a count of satellites in view, and satellites in use
                             **************************************************************/
                            mGpsStatus = locationManager.getGpsStatus(mGpsStatus);          
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

                            Log.e("Satellite " , "Inview : " + iTempCountInView);
                            Log.e("Satellite " , "Inuse : " + iTempCountInUse);
                        //    szSatellitesInView = String.valueOf(iTempCountInView);
                          //  szSatellitesInUse = String.valueOf(iTempCountInUse);
                        }
                    }
                }
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return location;
}}