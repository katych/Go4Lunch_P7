package com.example.go4lunch.model;

public class Worker {

    // FIELDS

private String nameWorker;
private String imageWorker;
private String placeId;
private String restaurantChoose;
private String emailWorker;

    // Empty constructor for Firebase
    public Worker() {
    }

    public Worker(String nameWorker, String imageWorker, String placeId, String restaurantChoose) {
        this.nameWorker = nameWorker;
        this.imageWorker = imageWorker;
        this.placeId = placeId;
        this.restaurantChoose = restaurantChoose;

    }
/////////////////////GETTERS//////////////////

    public String getNameWorker() {
        return nameWorker;
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
        this.nameWorker = name;
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
