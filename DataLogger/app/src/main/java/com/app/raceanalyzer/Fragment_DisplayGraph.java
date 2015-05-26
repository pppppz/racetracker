package com.app.raceanalyzer;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

public class Fragment_DisplayGraph extends Fragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_display_graph, container, false);
        LineChart chart = (LineChart) view.findViewById(R.id.chart);


        // get location & round id from fragment_chooseStartPoint
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            // then you have arguments
            long record_id = getArguments().getLong("record_id");
        } else {
            Log.e("location & record", " not set");
        }

        return view;
    }


}