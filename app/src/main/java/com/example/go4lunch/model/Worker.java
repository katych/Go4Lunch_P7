package com.example.go4lunch.model;

public class Worker {

    // FIELDS

private String workerName;
private String imageWorker;
private String placeId;
private String restaurantChoose;
private String emailWorker;

    // Empty constructor for Firebase
    public Worker() {
    }

    public Worker(String workerName, String imageWorker, String placeId, String restaurantChoose) {
        this.workerName = workerName;
        this.imageWorker = imageWorker;
        this.placeId = placeId;
        this.restaurantChoose = restaurantChoose;

    }
/////////////////////GETTERS//////////////////

    public String getNameWorker() {
        return workerName;
    }

    public  String getImageWorker() { return imageWorker; }

    public String getPlaceId() { return placeId; }

    public String getRestaurantChoose() {
        return restaurantChoose;
    }

    public String getEmailWorker() {
        return emailWorker;
    }


    //////////////////////SETTERS///////////////


    public void setName(String name) {
        this.workerName = name;
    }

    public void setImageWorker(String imageWorker) {
        this.imageWorker = imageWorker;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setRestaurantChoose(String restaurantChoose) {
        this.restaurantChoose = restaurantChoose;
    }

    public void setEmailWorker(String emailWorker) {
        this.emailWorker = emailWorker;
    }


}
