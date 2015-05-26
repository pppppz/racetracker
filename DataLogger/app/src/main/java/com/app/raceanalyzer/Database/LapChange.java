package com.app.raceanalyzer.Database;

public class LapChange {

    private String USER_ID;
    private double AXIS_X;
    private double AXIS_Y;
    private double AXIS_Z;
    private double VELOCITY;
    private int ROUND_ID;
    private double Lat;
    private double Lng;
    private int LAPCOUNT;
    private int HEADLAP_KEYID;


    public String getId() {
        return USER_ID;
    }

    public void setId(String id) {
        this.USER_ID = id;
    }

    public double getVelocity() {
        return VELOCITY;
    }

    public void setVelocity(double velocity) {
        this.VELOCITY = velocity;
    }

    public double getAXIS_X() {
        return AXIS_X;
    }

    public void setAXIS_X(double axis_x) {
        this.AXIS_X = axis_x;
    }

    public double getAXIS_Y() {
        return AXIS_Y;
    }

    public void setAXIS_Y(double axis_y) {
        this.AXIS_Y = axis_y;
    }

    public double getAXIS_Z() {
        return AXIS_Z;
    }

    public void setAXIS_Z(double axis_z) {
        this.AXIS_Z = axis_z;
    }

    public int getRoundId() {
        return ROUND_ID;
    }

    public void setRoundId(int roundid) {
        this.ROUND_ID = roundid;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        this.Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        this.Lng = lng;
    }

    public int getLAPCOUNT() {
        return LAPCOUNT;
    }

    public void setLAPCOUNT(int LAPCOUNT) {
        this.LAPCOUNT = LAPCOUNT;
    }

    public int getHeadLap_KEYID() {
        return HEADLAP_KEYID;
    }

    public void setHeadLap_KEYID(int headlapKeyID) {
        this.HEADLAP_KEYID = headlapKeyID;
    }


}