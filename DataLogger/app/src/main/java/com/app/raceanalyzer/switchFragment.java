package com.app.raceanalyzer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class switchFragment extends FragmentActivity {

    Fragment fragment;
    FragmentManager fragment_manager;


    public switchFragment(Fragment f, FragmentManager fm) {
        this.fragment = f;
        this.fragment_manager = fm;
    }

    public void doSwitch() {
        fragment_manager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }
}
