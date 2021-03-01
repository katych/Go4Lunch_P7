package com.example.go4lunch.model;


public class Position {

    //FIELDS
    private String title;
    private String placeId;
    private double lat;
    private double mLong;
    private boolean isChosen;

    //Constructor
    public Position(String title, String placeId, double lat, double aLong) {
        this.title = title;
        this.placeId = placeId;
        this.lat = lat;
        mLong = aLong;
        this.isChosen = false;
    }

    //GETTERS AND SETTER

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getTitle() {
        return title;
    }

    public double getLat() {
        return lat;
    }

    public double getLong() {
        return mLong;
    }

}
