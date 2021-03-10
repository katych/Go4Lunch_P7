package com.example.go4lunch.apiRestaurantPlaces;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.DetailRestaurant;
import com.example.go4lunch.model.Restaurant;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface NearbyPlaces {

    MutableLiveData<ArrayList<Restaurant>> configureRetrofitCall(LatLng latLng, String radius, String type);

    LiveData<DetailRestaurant> configureDetailRestaurant(String placeId);

}
