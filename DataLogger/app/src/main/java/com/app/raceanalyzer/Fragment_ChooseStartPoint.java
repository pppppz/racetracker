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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;


public class Fragment_ChooseStartPoint extends Fragment {

    //UI
    private TextView tvLatitude, tvLongitude;
    private Button btnSaveLocationToRecord;
    private View view;

    //material
    private ParseUser parseUSER;
    private GoogleMap myMap;
    private Marker marker;
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

            return false;
        }
    };
    private createRecordDB recordDB;
    private LatLng location = new LatLng(13.756037, 100.532185); //set start location (now set at digitopolis)
    /**
     * when press long clicked in map for marker into the map and set location to text field
     */
    GoogleMap.OnMapLongClickListener myOnMapLongClickListener =
            new GoogleMap.OnMapLongClickListener() {
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
    private long round_id;
    /**
     * listener button save -> send location to start record lap
     */
    Button.OnClickListener btnSaveLocationToRecordListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            //  bundle function is management resource , state

            //get username for create new record
            addDataToSQLite();

            Bundle bundle = new Bundle();
            bundle.putParcelable("location", location);
            bundle.putLong("round_id", round_id);

            // switch Fragment to start record lap
            Fragment fragment = new Fragment_StartRecordLap();
            fragment.setArguments(bundle);
            new switchFragment(fragment, getFragmentManager()).doSwitch();


        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** set UI */
        view = inflater.inflate(R.layout.fragment_choose_start_point, container, false);
        tvLatitude = (TextView) view.findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) view.findViewById(R.id.tvLongitude);
        btnSaveLocationToRecord = (Button) view.findViewById(R.id.btnSaveLocationToRecord);
        btnSaveLocationToRecord.setOnClickListener(btnSaveLocationToRecordListener);

        /** set map */
        setMap();

        /** create Record Activity */
        recordDB = new createRecordDB(getActivity());
        return view;
    }

    private void setLocationToTextView(LatLng location) {

        this.location = location;
        String latitude = String.valueOf(location.latitude);
        String longitude = String.valueOf(location.longitude);
        tvLatitude.setText("Latitude : " + latitude);
        tvLongitude.setText("Longitude : " + longitude);
    }

    private void setMap() {
        myMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        //listener
        myMap.setOnMyLocationButtonClickListener(myLocationButtonClickListener);
        myMap.setOnMapLongClickListener(myOnMapLongClickListener);

        //setting
        myMap.setMyLocationEnabled(true); //set current location
        myMap.moveCamera(CameraUpdateFactory.newLatLng(location)); //move camera to current
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15)); // can set 2-21
        marker = null;
    }


    protected void addDataToSQLite() {
        parseUSER = ParseUser.getCurrentUser();
        long insert_rowID = recordDB.addNewRecord(parseUSER.getUsername());
        Log.e(Fragment_ChooseStartPoint.class.getName(), "Lap id : " + insert_rowID);

        //the rowID of the newly inserted row, or -1 if an error occurred
        round_id = insert_rowID;
    }
}
