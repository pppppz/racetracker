package com.app.raceanalyzer;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.app.raceanalyzer.Database.RecordDB;

public class Fragment_ListRecord extends Fragment {

    private ListView listView;

    private View view;
    private SimpleCursorAdapter dbAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((CursorAdapter) listView.getAdapter()).getCursor().close();
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

        RecordDB record = new RecordDB(getActivity());
        Cursor recordList = record.readAllRecord();
        // Log.e(Fragment_ListRecord.class.getName(), RecordDB.USER_ID + "+" + RecordDB.ROUND_ID);
        String[] from = new String[]{RecordDB.RECORD_ID, RecordDB.CREATE_TIME};
        int[] target = new int[]{R.id.txtRecord, R.id.txtDate};
        dbAdapter = new SimpleCursorAdapter(getActivity(), R.layout.show_list_layout, recordList, from, target, 0);
        listView.setAdapter(dbAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor data = (Cursor) parent.getItemAtPosition(position);
                String record_id = data.getString(data.getColumnIndex(RecordDB.RECORD_ID));
                Toast.makeText(getActivity(), record_id, Toast.LENGTH_SHORT).show();
                long rid = Long.valueOf(record_id).longValue();
                Log.e("Fragment_ListRecord ", record_id);

                // switch Fragment to display data
                Bundle bundle = new Bundle(); /**  bundle function is management resource , state*/
                bundle.putLong("record_id", rid);
                Fragment fragment = new Fragment_DisplayData();
                fragment.setArguments(bundle);
                new switchFragment(fragment, getFragmentManager()).doSwitch();
            }
        });
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
        view = inflater.inflate(R.layout.fragment_choose_list_record, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        return view;
    }
}
