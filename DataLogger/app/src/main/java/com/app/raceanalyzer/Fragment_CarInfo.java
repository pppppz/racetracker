package com.app.raceanalyzer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment_CarInfo extends Fragment{

    private TextView tvLocation;

    private View view;
    private Activity a;
    private Button btnAddCar;




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
        //  outState.putString("currentURL", mURL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carinfo, container, false);



        btnAddCar = (Button) view.findViewById(R.id.btn_Add_New_Car);


        btnAddCar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Fragment fragment = new Fragment_AddCar();

                //create fragment manager for manage to switching fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container , fragment).commit();
            }
        });

        return view;

    }


}
