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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class Fragment_DisplayGraph extends Fragment {

    long record_id;
    long lapchoose;
    ImageButton.OnClickListener buttonLocationListener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            // switch Fragment to start record lap
            Bundle bundle = new Bundle(); //  bundle function is management resource , state
            bundle.putLong("lapchoose", lapchoose);
            bundle.putLong("record_id", record_id);
            Fragment fragment = new Fragment_DisplayData();
            fragment.setArguments(bundle);
            new switchFragment(fragment, getFragmentManager()).doSwitch();


        }
    };
    private View view;
    private Spinner spinner;
    private GraphView graph;
    Spinner.OnItemSelectedListener spinnerSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String getSelectText = parent.getSelectedItem().toString();
            long select = Long.parseLong(getSelectText);
            drawGraph(select);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    private ImageButton buttonLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        drawGraph(lapchoose);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_display_graph, container, false);
        graph = (GraphView) view.findViewById(R.id.graph);
        buttonLocation = (ImageButton) view.findViewById(R.id.ButtonMapAndTime);
        buttonLocation.setOnClickListener(buttonLocationListener);

        //set spinner and listener
        spinner = (Spinner) view.findViewById(R.id.spinnerLapID);
        spinner.setOnItemSelectedListener(spinnerSelectListener);

        // get location & round id from fragment_chooseStartPoint
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            // then you have arguments
            lapchoose = getArguments().getLong("lapchoose");
            record_id = getArguments().getLong("record_id");
        } else {
            Log.e("record id & lap choose", " not set");
        }

        setSpinnerHeadLap();
        return view;
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

    public void drawGraph(long select) {
        lapchoose = select;

        int count = 0;

        LineGraphSeries<DataPoint> series_x = new LineGraphSeries<>();
        series_x.setColor(Color.RED);
      /*  LineGraphSeries<DataPoint> series_y = new LineGraphSeries<>();
        series_y.setColor(Color.GREEN);
        LineGraphSeries<DataPoint> series_z = new LineGraphSeries<>();
        series_z.setColor(Color.BLUE);*/
        LapLocationChangeDB db = new LapLocationChangeDB(getActivity());
        List<LapChange> list = db.readDataLapChange(Resource.User, lapchoose, record_id);

        // loop for draw until finish
        for (LapChange lapChange : list) {
            series_x.appendData(new DataPoint(count, lapChange.getVelocity()), false, 200);
            //series_y.appendData(new DataPoint(count , lapChange.getAXIS_Y()) , false , 200);
            //series_z.appendData(new DataPoint(count , lapChange.getAXIS_Z()) , false , 200);
            count++;
        }


        graph.addSeries(series_x);
        //    graph.addSeries(series_y);
        //  graph.addSeries(series_z);

    }



}