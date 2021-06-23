package com.example.go4lunch.model;


import com.example.go4lunch.pojos.Location;

public class Restaurant  {

    private String uid;
    private String nameRestaurant;
    private Location mLocation;
    private String placeId;
    private String address;
    private String website;
    private String phoneNumber;
    private String image;
    private Boolean hour;
    private double rating;
    private int workers;
    private int distanceCurrentWorker;
    private boolean choice;


    //constructor
    public Restaurant(Location location, String name, String address, String placeId, boolean hour, String urlImage,
            int distance, int workers, double rating ) {
        this.mLocation = location;
        this.nameRestaurant = name;
        this.placeId = placeId;
        this.address = address;
        this.hour = hour;
        this.image = urlImage;
        this.distanceCurrentWorker = distance;
        this.workers = workers;
        this.rating = rating;
        this.choice = false;
    }

    public Restaurant() {
    }

    /**
     * Constructor to create a Restaurant in Firebase
     */
    public Restaurant(String uid,String name,String placeId, String address,String image,double rating  )
    {
        this.uid=uid;
        this.placeId = placeId;
        this.nameRestaurant = name;
        this.address = address;
        this.image = image;
        this.rating= rating;
        this.choice=false;
    }

    /////////////////////getters/////////////

    public String getUid() {
        return uid;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public Location getLocation() {
        return mLocation;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public Boolean getHour() {
        return hour;
    }

    public double getRating() {
        return rating;
    }

    public int getWorkers() {
        return workers;
    }

    public boolean isChoice() {
        return choice;
    }

    public int getDistanceCurrentWorker() {
        return distanceCurrentWorker;
    }



    //////////////////////SETTERS//////////////////////


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public void setName(String name) {
        this.nameRestaurant = name;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setHour(Boolean openNow) {
        this.hour = openNow;
    }


    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setDistanceCurrentWorker(int distanceCurrentWorker) {
        this.distanceCurrentWorker = distanceCurrentWorker;
    }
}
