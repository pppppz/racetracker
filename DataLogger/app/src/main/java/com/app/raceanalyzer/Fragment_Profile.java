package com.app.raceanalyzer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseUser;


public class Fragment_Profile extends Fragment {


    private TextView tv_Name;
    private RelativeLayout llLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        llLayout    = (RelativeLayout) inflater.inflate(R.layout.fragment_profile, container, false);
        tv_Name = (TextView) llLayout.findViewById(R.id.tv_name);

        //ivImage.setOnClickListener(onClickImage);
        tv_Name.setText("Name : " + retrieveName());


        Log.e("test title " , retrieveName());
        return llLayout;
    }

    public String retrieveName()  {

        ParseUser user = ParseUser.getCurrentUser();
        return user.getUsername();
    }


}