package com.app.raceanalyzer.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RaceAnalyzer";
    private static final int DATABASE_VERSION = 11;

    private static String DATABASE_RECORD = "create table Record (`round_id` INTEGER PRIMARY KEY AUTOINCREMENT , `user_id` VARCHAR(20) not null , `creation_time` DATETIME DEFAULT CURRENT_TIMESTAMP , `fastest_Lap` INTEGER)";
    private static String DATABASE_LAP_LOCATION_CHANGE = "create table LapLocationChange (`lapLocationChangeID` INTEGER PRIMARY KEY AUTOINCREMENT, `axis_x` INTEGER  not null, `axis_y` INTEGER  not null, `axis_z` INTEGER  not null , `velocity` INTEGER not null, `latitude` INTEGER not null , `longitude` INTEGER  not null, `round_id` INTEGER not null , `time` INTEGER , `user_id` VARCHAR(20) not null , `lap_id` INTEGER)";
    private static String DATABASE_LAP_HEADER = "create table HeadLap(`lap_id` INTEGER PRIMARY KEY AUTOINCREMENT,`user_id` VARCHAR(20) , `round_id` INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_RECORD);
        db.execSQL(DATABASE_LAP_LOCATION_CHANGE);
        db.execSQL(DATABASE_LAP_HEADER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(DatabaseHelper.class.getName(), "Upgrade database version from version" + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXIST Record;");
        db.execSQL("DROP TABLE IF EXIST LapLocationChange;");
        db.execSQL("DROP TABLE IF EXIST LapHeader;");
        onCreate(db);
    }
}