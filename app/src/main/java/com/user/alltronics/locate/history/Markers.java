package com.user.alltronics.locate.history;
/*
 * This is our model class and it corresponds to Markers table in database
 */

public class Markers {

    int id;
    String latitude;
    String longitude;


    public Markers() {
        super();
    }
    public Markers(int i, String latitude, String longitude) {
        super();
        this.id = i;
        this.latitude = latitude;
        this.longitude=longitude;

    }

    // constructor
    public Markers(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String endtime) {
        this.longitude = endtime;
    }

}
