package com.example.go4lunch.model;


public class DetailRestaurant {

    //FIELDS
    private String formatted_address;
    private String formatted_phone_number;
    private String name;
    private String photo;
    private double rating;
    private String website;

    //Constructor
    public DetailRestaurant(String formatted_address, String formatted_phone_number, String name,
                            String photoReference, double rating, String website) {
        this.formatted_address = formatted_address;
        this.formatted_phone_number = formatted_phone_number;
        this.name = name;
        this.photo = photoReference;
        this.rating = rating;
        this.website = website;
    }

    //GETTERS

    public String getFormatted_address() {
        return formatted_address;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public String getPhotoReference() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public String getWebsite() {
        return website;
    }
}
