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
    long lap_choose;
    //listener button Location
    ImageButton.OnClickListener buttonLocationListener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            /** send data switch to display data */
            Bundle bundle = new Bundle(); //  bundle function is management resource , state
            bundle.putLong("lap_choose", lap_choose);
            bundle.putLong("record_id", record_id);
            Fragment fragment = new Fragment_DisplayData();
            fragment.setArguments(bundle);
            new switchFragment(fragment, getFragmentManager()).doSwitch();
        }
    };
    private int dataTypePosition;
    private View view;
    private Spinner spinnerHeadLap, spinnerDataType;
    private GraphView graph;
    private LineGraphSeries<DataPoint> line;
    //listener spinner data type
    Spinner.OnItemSelectedListener spinnerTypeDataListener = new AdapterView.OnItemSelectedListener() {
        // data type (reference position by data_type array in string.xml)
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            dataTypePosition = position;
            drawGraph(lap_choose, position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    //listener spinner head lap
    Spinner.OnItemSelectedListener spinnerHeadLapListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String getSelectText = parent.getSelectedItem().toString();
            long select = Long.parseLong(getSelectText);
            drawGraph(select, spinnerDataType.getSelectedItemPosition());
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
        drawGraph(lap_choose, 0);
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


        /** ----------------------------------DECLARE UI--------------------------------- */

        //button
        buttonLocation = (ImageButton) view.findViewById(R.id.ButtonMapAndTime);
        buttonLocation.setOnClickListener(buttonLocationListener);

        //set head lap spinner and listener
        spinnerHeadLap = (Spinner) view.findViewById(R.id.spinnerLapID);
        spinnerHeadLap.setOnItemSelectedListener(spinnerHeadLapListener);

        //set data type spinner & listener for
        spinnerDataType = (Spinner) view.findViewById(R.id.spinnerTypeData);
        spinnerDataType.setOnItemSelectedListener(spinnerTypeDataListener);

        /** ----------------------------FINISH DECLARE UI--------------------------------------- */
        // get location & round id from fragment_chooseStartPoint
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            // then you have arguments
            lap_choose = getArguments().getLong("lap_choose");
            record_id = getArguments().getLong("record_id");
        } else {
            Log.e("record id & lap choose", " not set");
        }

        setSpinnerHeadLap();
        setSpinnerDataType();
        return view;
    }

    public void drawGraph(long select, int dataType) {

        //cursor select = lap
        lap_choose = select;

        //count index of graph
        int count = 0;

        //clear graph , query data
        graph.removeAllSeries();
        line = new LineGraphSeries<>();
        LapLocationChangeDB db = new LapLocationChangeDB(getActivity());
        List<LapChange> list = db.readDataLapChange(Resource.User, lap_choose, record_id);

        // loop for draw graph until finish
        for (LapChange lapChange : list) {
            if (dataType == 0) {
                line.appendData(new DataPoint(count, lapChange.getAXIS_X()), false, 200);
                line.setColor(Color.RED);
            } else if (dataType == 1) {
                line.appendData(new DataPoint(count, lapChange.getAXIS_Y()), false, 200);
                line.setColor(Color.GREEN);
            } else if (dataType == 2) {
                line.appendData(new DataPoint(count, lapChange.getAXIS_Z()), false, 200);
                line.setColor(Color.BLUE);
            } else if (dataType == 3) {
                line.appendData(new DataPoint(count, lapChange.getVelocity()), true, 200);
                line.setColor(Color.CYAN);
            } else {
                Log.e(Fragment_DisplayGraph.class.getName(), "data not found");
            }
            count++;
        }

        //add line into graph
        graph.addSeries(line);
    }

    private void setSpinnerDataType() {
        //get array list from string.xml to set data type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.data_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataType.setAdapter(adapter);
    }

    private void setSpinnerHeadLap() {
        //get lap from head lap for choose lap
        HeadLapDatabase db = new HeadLapDatabase(getActivity());
        List<Long> labels = db.readAllHeadLap(Resource.User, record_id);
        ArrayAdapter<Long> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeadLap.setAdapter(dataAdapter);
    }
}