package com.example.go4lunch.model;


public class Restaurant {

    private String nameRestaurant;
    private String placeId;
    private String address;
    private String website;
    private String phoneNumber;
    private String image;
    private Boolean openNow;
    private double rating;
    private int workers;
    private int distanceCurrentWorker;
    private boolean choice;


    //Empty constructor for Firebase
    public Restaurant() {
    }

    //constructor
    public Restaurant( String name, String address, String placeId, boolean openNow, String urlImage,
            int distance, int workers, double rating) {
        this.nameRestaurant = name;
        this.placeId = placeId;
        this.address = address;
        this.openNow = openNow;
        this.image = urlImage;
        this.distanceCurrentWorker = distance;
        this.workers = workers;
        this.rating = rating;
        this.choice = false;
    }

    /**
     * Constructor to create a Restaurant in Firebase
     */
    public Restaurant(String placeId, int workers, String name, String address)
    {
        this.placeId = placeId;
        this.workers = workers;
        this.nameRestaurant = name;
        this.address = address;
    }

    /////////////////////getters/////////////

    public String getNameRestaurant() {
        return nameRestaurant;
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

    public Boolean getOpenNow() {
        return openNow;
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

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
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

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }


    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setDistanceCurrentWorker(int distanceCurrentWorker) {
        this.distanceCurrentWorker = distanceCurrentWorker;
    }
}
