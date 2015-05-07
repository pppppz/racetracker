package com.app.raceanalyzer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Fragment_ChooseStartPoint extends Fragment {

    private GoogleMap myMap;
    private TextView tvLocation;
    private Button btnSaveLocationToRecord;
    private View view;

    private Marker marker;

    //set new LatLng
    private LatLng location = new LatLng(13.756037, 100.532185);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_choose_start_point, container, false);
        tvLocation = (TextView) view.findViewById(R.id.tvSetLocation);
        btnSaveLocationToRecord = (Button) view.findViewById(R.id.btnSaveLocationToRecord);
        btnSaveLocationToRecord.setOnClickListener(btnSaveLocationToRecordListener);
        setMap();

        // Now auto clicking the button
        //    btntemp.performClick();
        return view;
    }

    private void setMap() {

        myMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        myMap.setMyLocationEnabled(true);
        myMap.setOnMyLocationButtonClickListener(myLocationButtonClickListener);
        myMap.setOnMapLongClickListener(myOnMapLongClickListener);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        marker = null;
    }

    GoogleMap.OnMyLocationButtonClickListener myLocationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override

        public boolean onMyLocationButtonClick() {
            //create mark point and set text

            if (marker != null) {
                myMap.clear();
            }

            String latitude = String.valueOf(location.latitude);
            String longitude = String.valueOf(location.longitude);
            tvLocation.setText("Latitude : " + latitude + " Longitude : " + longitude);
            return false;
        }
    };

    /**
     * listener when click button save -> send location to start lap page
     */
    Button.OnClickListener btnSaveLocationToRecordListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("location", location);
            //set Fragmentclass Arguments
            Fragment fragment = new Fragment_StartRecordLap();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    };

    /** when long clicked will mark into the map and set location to text field */
    GoogleMap.OnMapLongClickListener myOnMapLongClickListener =
            new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {
                    //clear old mark , add new mark , set text to display to screen , set location
                    myMap.clear();
                    marker = myMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
                    tvLocation.setText(point.toString());
                    location = point;
                }
            };
}