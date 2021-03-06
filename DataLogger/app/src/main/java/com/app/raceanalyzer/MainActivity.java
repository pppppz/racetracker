package com.app.raceanalyzer;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.raceanalyzer.adapter.NavDrawerListAdapter;
import com.app.raceanalyzer.model.NavDrawerItem;
import com.app.raceanalyzer.parse.Car;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private DrawerLayout DrawerLayout;
    private ListView DrawerList;
    private ActionBarDrawerToggle DrawerToggle;

    // nav drawer title
    private CharSequence DrawerTitle;

    // used to store app title
    private CharSequence Title;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get user data and check log in
        parse();
        drawAllUI(savedInstanceState);

        //keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void drawAllUI(Bundle savedInstanceState) {

        // load slide menu items
        Title = DrawerTitle = getTitle();
        drawDrawer(); // drawer is a list of menu when swipe
        enableHomeButtonOnActionBar();
        forceUse_softMenu();
        setToggle();
        setFirstState(savedInstanceState);

    }

    private void setFirstState(Bundle state) {
        // if instance state is null choose first nav item (position = 0) for display
        if (state == null) {
            displayView(0);
        }
    }

    private void forceUse_softMenu() {

        /** force use soft menu key **/
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getLocalizedMessage());
        }
    }


    private void enableHomeButtonOnActionBar() {
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }


    private void setToggle() {
        //set title of drawer (left swipe)
        DrawerToggle = new ActionBarDrawerToggle(this, DrawerLayout,
                //   R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(Title);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(DrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        DrawerLayout.setDrawerListener(DrawerToggle);
    }

    private void drawDrawer() {


        // declare xml -> java
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerList = (ListView) findViewById(R.id.list_slidermenu);

        /** get string array from strings.xml to set title into drawer list */
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);


        // adding nav drawer items to array
        navDrawerItems = new ArrayList<>();

        /** --------- Add title and icon into drawer --------  **/

        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Car info
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Fragment_Record  , Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, "22"));
        // Fragment_Record  , Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "55"));


        // Recycle the typed array
        navMenuIcons.recycle();

        //set event listener
        DrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        DrawerList.setAdapter(adapter);
    }


    private void parse() {

        // start initial parse with key
        Parse.initialize(this, "ZjIlFWUT086C0Y5HhogkKvxuZ8KTy5Z7MH4AX7me", "4FQJ0QqB6jdxFOWdrFSY3C3pZN2LTw0vk2wrog9T");
        //parse analytic is track
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        //register class use in project such as this project use table Car in parse also register subclass Car (in Project)
        ParseObject.registerSubclass(Car.class);

        // get user data if null go to login
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = DrawerLayout.isDrawerOpen(DrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new Fragment_CreateRecord();
                break;
            case 1:
                fragment = new Fragment_CarInfo();
                break;
            case 2:
                fragment = new Fragment_Profile();
                break;
            case 3:
                fragment = new Fragment_ListRecord();
                break;
            default:
                fragment = new Fragment_CreateRecord();
                break;
        }

        //create fragment manager for manage to switching fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        new switchFragment(fragment, fragmentManager).doSwitch();

        // update selected item and title, then close the drawer
        DrawerList.setItemChecked(position, true);
        DrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        DrawerLayout.closeDrawer(DrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        Title = title;
        getSupportActionBar().setTitle(Title);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        DrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        DrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate main menu into menu
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //create choice menu in action bar eg. Logout , Setting
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (DrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

}
