package com.app.raceanalyzer;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.raceanalyzer.Database.HeadLapDatabase;

public class Fragment_SaveToCloud extends Fragment {

    private View view;
    private TextView tvExample;
    private HeadLapDatabase headlap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_save_to_cloud, container, false);
        tvExample = (TextView) view.findViewById(R.id.txtExample);
        // get location & round id from fragment_chooseStartPoint
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            // then you have arguments
            long timeLap = getArguments().getLong("timelap");
            long record_id = getArguments().getLong("record_id");
            int lap_count = getArguments().getInt("lap_count");
            int sec = (int) (timeLap / 1000);
            int min = sec / 60;
            sec = sec % 60;
            int milliseconds = (int) (timeLap % 1000);
            tvExample.setText("" + min + ":"
                    + String.format("%02d", sec) + ":" + String.format("%03d", milliseconds));
        } else {
            Log.e("location & round", " not set");
        }
        return view;
    }
}
