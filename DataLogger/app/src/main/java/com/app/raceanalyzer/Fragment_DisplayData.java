package com.app.raceanalyzer;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.app.raceanalyzer.Database.HeadLapDatabase;
import com.app.raceanalyzer.Database.LapChange;
import com.app.raceanalyzer.Database.LapLocationChangeDB;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class Fragment_DisplayData extends Fragment {

    public static long lap_choose;
    private ImageButton btnGraph;
    private Spinner spinner;
    private View view;
    private long record_id;
    ImageButton.OnClickListener btnGraphListener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View v) {

            // switch Fragment to start record lap
            Bundle bundle = new Bundle(); //  bundle function is management resource , state
            bundle.putLong("lap_choose", lap_choose);
            bundle.putLong("record_id", record_id);
            Fragment fragment = new Fragment_DisplayGraph();
            fragment.setArguments(bundle);
            new switchFragment(fragment, getFragmentManager()).doSwitch();
        }
    };
    private GoogleMap myMap;
    private Polyline line;
    Spinner.OnItemSelectedListener spinnerSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String getSelectText = parent.getSelectedItem().toString();
            lap_choose = Long.parseLong(getSelectText);
            drawFromQuery(lap_choose);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_display_data, container, false);

        //set spinner and listener
        spinner = (Spinner) view.findViewById(R.id.spinnerLapID);
        spinner.setOnItemSelectedListener(spinnerSelectListener);

        //set image graph
        btnGraph = (ImageButton) view.findViewById(R.id.btnGraph);
        btnGraph.setOnClickListener(btnGraphListener);

        // get location & round id from fragment_chooseStartPoint
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            // then you have arguments
            record_id = getArguments().getLong("record_id");
            lap_choose = getArguments().getLong("lap_choose");
        } else {
            Log.e("lap choose & record", " not set");
        }


        drawMap();
        setSpinnerHeadLap();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setSpinnerHeadLap() {

        HeadLapDatabase db = new HeadLapDatabase(getActivity());
        List<Long> labels = db.readAllHeadLap(Resource.User, record_id);

        // Creating adapter for spinner
        ArrayAdapter<Long> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


    private void drawFromQuery(long lap_count) {

        lap_choose = lap_count;
        LatLng beforeDestination = null;

        //if line is exist remove it before render
        //  line.remove();

        //declare database to list
        LapLocationChangeDB db = new LapLocationChangeDB(getActivity());
        List<LapChange> list = db.readDataLapChange(Resource.User, lap_choose, record_id);

        // loop for draw until finish
        for (LapChange lapChange : list) {
            LatLng afterDestination = new LatLng(lapChange.getLat(), lapChange.getLng());

            //if first time create buffer location and move camera to that location
            if (beforeDestination == null) {
                beforeDestination = new LatLng(lapChange.getLat(), lapChange.getLng());
                myMap.moveCamera(CameraUpdateFactory.newLatLng(beforeDestination)); //move camera to current
                myMap.animateCamera(CameraUpdateFactory.zoomTo(2)); // can set 2-21
            } else {
                //if not first time -> draw one point to one point
                drawPolyLine(beforeDestination, afterDestination);
                Log.i("Polyline : ", String.valueOf(beforeDestination) + "+" + afterDestination);

                //set latest destination to before destination (swap)
                beforeDestination = afterDestination;
            }
        }
    }

    private void drawMap() {
        myMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        myMap.setMyLocationEnabled(true);
    }

    public void drawPolyLine(LatLng startDestination, LatLng endDestination) {
        Log.e("draw polyline start:", String.valueOf(startDestination));
        Log.e("draw polyline end:", String.valueOf(endDestination));
        line = myMap.addPolyline(new PolylineOptions().add
                (new LatLng(startDestination.latitude, startDestination.longitude)
                        , new LatLng(endDestination.latitude, endDestination.longitude))
                .width(5).color(Color.RED));
    }
}