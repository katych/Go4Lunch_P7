package com.example.go4lunch.model;

public class Worker {

    // FIELDS

private String nameWorker;
private String imageWorker;
private String placeId;
private String restaurantName;
private String emailWorker;

    // Empty constructor for Firebase
    public Worker() {
    }

    public Worker(String nameWorker, String imageWorker, String placeId, String restaurantName) {
        this.nameWorker = nameWorker;
        this.imageWorker = imageWorker;
        this.placeId = placeId;
        this.restaurantName = restaurantName;

    }
/////////////////////GETTERS//////////////////

    public String getNameWorker() {
        return nameWorker;
    }

    public  String getImageWorker() { return imageWorker; }

    public String getPlaceId() { return placeId; }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getEmailWorker() {
        return emailWorker;
    }


    //////////////////////SETTERS///////////////


    public void setName(String name) {
        this.nameWorker = name;
    }

    public void setImageWorker(String imageWorker) {
        this.imageWorker = imageWorker;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setRestaurantChoose(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setEmailWorker(String emailWorker) {
        this.emailWorker = emailWorker;
    }


}
