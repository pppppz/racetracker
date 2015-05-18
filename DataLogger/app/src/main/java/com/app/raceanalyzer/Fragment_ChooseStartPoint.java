package com.app.raceanalyzer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.raceanalyzer.Database.createRecordDB;
import com.app.raceanalyzer.SensorListener.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;


public class Fragment_ChooseStartPoint extends Fragment {

    private long round_id;

    //UI
    private TextView tvLatitude, tvLongitude;
    private Button btnSaveLocationToRecord;
    private View view;
    private createRecordDB recordDB;

    //material
    private ParseUser parseUSER;
    private GoogleMap myMap;
    private Marker marker;
    private LatLng location;

    /**
     * create marker point and set text
     */
    GoogleMap.OnMyLocationButtonClickListener myLocationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override
        public boolean onMyLocationButtonClick() {
            //clear exist marker
            if (marker != null) {
                myMap.clear();
            }

            //set latitude , longitude to text field
            setLocationToTextView(LocationListener.CurrentLocation(getActivity()));
            return false;
        }
    };
    /**
     * when press long clicked in map for marker into the map and set location to text field
     */
    GoogleMap.OnMapLongClickListener myOnMapLongClickListener = new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {
                    //clear exist marker
                    myMap.clear();

                    //add new marker , set text to display to screen , set location
                    marker = myMap.addMarker(new MarkerOptions().position(point).title(point.toString()));

                    //set latitude , longitude to text field
                    setLocationToTextView(point);
                }
            };

    /**
     * listener button save -> send location to start record lap
     */
    Button.OnClickListener btnSaveLocationToRecordListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            addDataToSQLite();
            Bundle bundle = new Bundle(); //  bundle function is management resource , state
            bundle.putParcelable("location", location);
            bundle.putLong("round_id", round_id);

            // switch Fragment to start record lap
            Fragment fragment = new Fragment_StartRecordLap();
            fragment.setArguments(bundle);
            new switchFragment(fragment, getFragmentManager()).doSwitch();


        }
    };


    // load UI
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set UI
        view = inflater.inflate(R.layout.fragment_choose_start_point, container, false);
        tvLatitude = (TextView) view.findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) view.findViewById(R.id.tvLongitude);
        btnSaveLocationToRecord = (Button) view.findViewById(R.id.btnSaveLocationToRecord);
        btnSaveLocationToRecord.setOnClickListener(btnSaveLocationToRecordListener);

        //set map
        setMap();

        //create Record Activity
        recordDB = new createRecordDB(getActivity());
        return view;
    }

    private void setMap() {
        myMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        myMap.setMyLocationEnabled(true); //enable permission current location

        //listener
        myMap.setOnMyLocationButtonClickListener(myLocationButtonClickListener);
        myMap.setOnMapLongClickListener(myOnMapLongClickListener);

        //get current location and set into map and text view
        location = LocationListener.CurrentLocation(getActivity());
        myMap.moveCamera(CameraUpdateFactory.newLatLng(location)); //move camera to current
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15)); // can set 2-21
        marker = null;
        tvLatitude.setText("Latitude : " + location.latitude);
        tvLongitude.setText("Longitude : " + location.longitude);
    }


    protected void addDataToSQLite() {
        parseUSER = ParseUser.getCurrentUser();
        long insert_rowID = recordDB.addNewRecord(parseUSER.getUsername());
        Log.e(Fragment_ChooseStartPoint.class.getName(), "Lap id : " + insert_rowID);

        //the rowID of the newly inserted row, or -1 if an error occurred
        round_id = insert_rowID;
    }

    private void setLocationToTextView(LatLng location) {
        this.location = location;
        tvLatitude.setText("Latitude : " + location.latitude);
        tvLongitude.setText("Longitude : " + location.longitude);
    }
}
