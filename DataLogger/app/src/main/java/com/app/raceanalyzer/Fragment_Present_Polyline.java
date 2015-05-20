package com.app.raceanalyzer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.raceanalyzer.Database.LapLocationChangeDB;
import com.app.raceanalyzer.Database.lapChangeAdapter;
import com.app.raceanalyzer.SensorListener.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseUser;

import java.util.List;


public class Fragment_Present_Polyline extends Fragment {

    //UI

    LatLng start = new LatLng(13.748223, 100.533957); //BTS Siam
    LatLng ratchamung = new LatLng(13.756164, 100.618855);
    /**
     * create marker point and set text
     */
    GoogleMap.OnMyLocationButtonClickListener myLocationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override
        public boolean onMyLocationButtonClick() {

            return false;
        }
    };
    private View view;
    private LapLocationChangeDB db;    //material
    private ParseUser parseUSER;
    private GoogleMap myMap;
    private LatLng location;

    // load UI
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set UI
        view = inflater.inflate(R.layout.fragment_choose_start_point, container, false);
        //set map

        setMap();


        Polyline line = myMap.addPolyline(new PolylineOptions()
                .add(start, ratchamung)
                .width(5)
                .color(Color.RED));

        List<lapChangeAdapter> list = db.getAllLapChange();

        return view;
    }

    private void setMap() {
        myMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        myMap.setMyLocationEnabled(true); //enable permission current location

        //listener
        myMap.setOnMyLocationButtonClickListener(myLocationButtonClickListener);

        //get current location and set into map and text view
        location = LocationListener.CurrentLocation(getActivity());
        myMap.moveCamera(CameraUpdateFactory.newLatLng(location)); //move camera to current
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15)); // can set 2-21


    }


}
