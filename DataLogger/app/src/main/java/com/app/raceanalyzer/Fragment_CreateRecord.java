package com.app.raceanalyzer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
public class Fragment_CreateRecord extends Fragment {

    private Button btnChooseStartPoint;
    private View view;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = super.getActivity();
        view = inflater.inflate(R.layout.fragment_create_record, container, false);
        btnChooseStartPoint = (Button) view.findViewById(R.id.btnChooseStartPoint);
        btnChooseStartPoint.setOnClickListener(chooseStartPointListener);
        return view;
    }

    Button.OnClickListener chooseStartPointListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            new switchFragment(new Fragment_ChooseStartPoint(), fragmentManager).doSwitch();
        }
    };

}
