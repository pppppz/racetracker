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
import com.google.android.gms.maps.model.MarkerOptions;


public class Fragment_ChooseLocationStartOrFinish extends Fragment {
    LatLng location;
    private GoogleMap myMap;
    private TextView tvLocation;
    private Button btnSaveLocationToRecord;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chooselocationstartorfinish, container, false);
        tvLocation = (TextView) view.findViewById(R.id.tvSetLocation);
        btnSaveLocationToRecord = (Button) view.findViewById(R.id.btnSaveLocationToRecord);
        myMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        myMap.setMyLocationEnabled(true);
        myMap.setOnMyLocationButtonClickListener(myLocationButtonClickListener);
        myMap.setOnMapLongClickListener(myOnMapLongClickListener);
        myMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        btnSaveLocationToRecord.setOnClickListener(btnSaveLocationToRecordListener);

        // Now auto clicking the button
        //    btntemp.performClick();
        return view;
    }

    GoogleMap.OnMyLocationButtonClickListener myLocationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override

        public boolean onMyLocationButtonClick() {
            //create mark point and set text
            String latitude = String.valueOf(location.latitude);
            String longitude = String.valueOf(location.longitude);
            tvLocation.setText("Latitude : " + latitude + "Longitude : " + longitude);
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
            Fragment fragment = new Fragment_Record_Lap();
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
                    myMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
                    tvLocation.setText(point.toString());
                    location = point;
                }
            };
}