package com.app.raceanalyzer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.raceanalyzer.Database.RecordDB;
import com.app.raceanalyzer.SensorListener.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseUser;

public class Fragment_CreateRecord extends Fragment {

    /**
     * button to switch to fragment choose map
     */
    Button.OnClickListener chooseStartPointListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            new switchFragment(new Fragment_ChooseStartPoint(), fragmentManager).doSwitch();
        }
    };
    private Button btnChooseStartPoint;
    private Button btnStartFromCurrent;
    private View view;
    private long record_id;
    //database
    private RecordDB RecordDB;
    Button.OnClickListener startFromCurrentListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            //create record return index which equal record_id
            record_id = createRecordToSQLite();
            LatLng location = LocationListener.CurrentLocation(getActivity());

            // switch Fragment to start record
            Bundle bundle = new Bundle(); //  bundle function is management resource , state
            bundle.putParcelable("location", location);
            bundle.putLong("record_id", record_id);
            Fragment fragment = new Fragment_StartRecordLap();
            fragment.setArguments(bundle);
            new switchFragment(fragment, getFragmentManager()).doSwitch();

        }
    };

    private long createRecordToSQLite() {
        long insert_rowID = RecordDB.addNewRecord(ParseUser.getCurrentUser().getUsername());
        Log.e(Fragment_ChooseStartPoint.class.getName(), "Record id : " + insert_rowID);
        //the rowID of the newly inserted row, or -1 if an error occurred
        return insert_rowID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_record, container, false);

        //declare button choose start point
        btnChooseStartPoint = (Button) view.findViewById(R.id.btnChooseStartPoint);
        btnChooseStartPoint.setOnClickListener(chooseStartPointListener);

        //declare button from current location
        btnStartFromCurrent = (Button) view.findViewById(R.id.btnStartFromCurrent);
        btnStartFromCurrent.setOnClickListener(startFromCurrentListener);

        //create Record Activity
        RecordDB = new RecordDB(getActivity());

        return view;
    }

}
