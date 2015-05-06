package com.app.raceanalyzer;

import android.os.Bundle;
import android.support.v4.app.Fragment;


public class UserFragment extends Fragment {
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getLong(USER_ID);
        if(user==null){
            //
            // Recreating here user from user id(i.e requesting from your data model,
            // which could be services, direct request to rest, or data layer sitting
            // on application model
            //
            user = bringUser();
        }
    }


    public static UserFragment newInstance(User user, long user_id){
        UserFragment userFragment = new UserFragment();
        Bundle args = new Bundle();
        args.putLong(USER_ID,user_id);
        if(user!=null){
            userFragment.user=user;
        }
        userFragment.setArguments(args);
        return userFragment;

    }

    public static UserFragment newInstance(long user_id){
        return newInstance(null,user_id);
    }

    public static UserFragment newInstance(User user){
        return newInstance(user,user.id);
    }*/
}