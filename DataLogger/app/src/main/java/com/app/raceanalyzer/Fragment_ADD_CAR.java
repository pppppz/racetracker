package com.app.raceanalyzer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raceanalyzer.parse.Car;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class Fragment_ADD_CAR extends Fragment {

    private TextView tvLocation;

    private View view;
    private Activity a;

    private Button btnSave;

    private EditText et_Version;
    private EditText et_hp;
    private EditText et_hp_rpm;
    private EditText et_front_rim_size;
    private EditText et_front_tires_size;
    private EditText et_rear_rim_size;
    private EditText et_rear_tires_size;
    private EditText et_driveline_layout;
    private EditText et_weight_front_percent;
    private EditText et_height;
    private EditText et_weight_all;
    private EditText et_wheelbase;
    private EditText et_gear_ratio;
    private EditText et_torque_nm;
    private EditText et_torque_rpm;
    private EditText et_Traction_control;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
      //  menu.clear();

        inflater.inflate(R.menu.save_car, menu);


    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        /** set visible menu **/
        menu.findItem(R.id.save_car).setVisible(true);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }


    private int getInt(EditText editText) throws NumberFormatException {
        return Integer.valueOf(editText.getText().toString());
    }

    private String getString(EditText editText)  {
        return editText.getText().toString();
    }

    public void saveCar() {

        if (et_Version.getText().length() > 0) {

            //get data from task_input and set data by method in task.class

            Car c = new Car();
            c.setACL(new ParseACL(ParseUser.getCurrentUser()));


            Log.e("add car", et_torque_nm.getText().toString());
            Log.e("add car" , et_torque_rpm.getText().toString());

            //user and version of car
            c.setUser(ParseUser.getCurrentUser());
            c.setModel(String.valueOf(et_Version.getText()));

            //engine
        //    c.setTorque_power(getInt(et_torque_nm));
          //  c.setTorque_round(getInt(et_torque_rpm));
            //c.setHorse_power_hp(getInt(et_hp));
          //  c.setHorse_power_round(getInt(et_hp_rpm));

            //wheel
            c.setRimBack(getString(et_rear_rim_size));
            c.setRimFront(getString(et_front_rim_size));
            c.setTiresFront(getString(et_front_tires_size));
            c.setTiresRear(getString(et_rear_tires_size));

            //body & axle
            c.setDriveLineLayout(getString(et_driveline_layout));
//            c.setHeight(getInt(et_height));
  //          c.setWheelbase(getInt(et_wheelbase));
    //        c.setWeightFront(getInt(et_weight_front_percent));

            //others
            c.setTractionControl(getString(et_Traction_control));

            c.saveEventually(); // save in to parse.com





            Toast.makeText(super.getActivity(), "Add car completed", Toast.LENGTH_SHORT).show();




            Fragment fragment = new Fragment_CarInfo();
            FragmentManager fragmentManager = super.getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }

    }


    //create choice menu in action bar eg. Logout , Setting
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_car:
                saveCar();
                return true;
        }
        return false;
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


    private void declareUI(){
        //version
        et_Version = (EditText) view.findViewById(R.id.et_Version);

        //engine
        et_torque_nm = (EditText) view.findViewById(R.id.et_torque_nm);
        et_torque_rpm = (EditText) view.findViewById(R.id.et_torque_rpm);
        et_hp = (EditText) view.findViewById(R.id.et_hp);
        et_hp_rpm = (EditText) view.findViewById(R.id.et_hp_rpm);

        //wheel (tires , rims)
        et_front_rim_size = (EditText) view.findViewById(R.id.et_front_rim_size);
        et_rear_rim_size = (EditText) view.findViewById(R.id.et_rear_rim_size);
        et_front_tires_size = (EditText) view.findViewById(R.id.et_front_tires_size);
        et_rear_tires_size = (EditText) view.findViewById(R.id.et_rear_tires_size);

        //body
        et_driveline_layout = (EditText) view.findViewById(R.id.et_driveline_layout);
        et_height = (EditText) view.findViewById(R.id.et_height);
        et_weight_all = (EditText) view.findViewById(R.id.et_weight_all);
        et_wheelbase = (EditText) view.findViewById(R.id.et_wheelbase);
        et_weight_front_percent = (EditText) view.findViewById(R.id.et_weight_front_percent);

        //others
        et_Traction_control = (EditText) view.findViewById(R.id.et_traction_control);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //resource
        view = inflater.inflate(R.layout.fragment_addcar, container, false);
        a = super.getActivity();
        declareUI();
        return view;

    }
}
