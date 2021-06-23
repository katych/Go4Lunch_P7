package com.example.go4lunch.apiRestaurantPlaces;

import com.example.go4lunch.pojos.Detail;
import com.example.go4lunch.pojos.RestaurantsResult;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RestaurantService {

    /**
     * Request HTTP in Json to have nearby Restaurants
     * GET call for retrofit
     */
    @GET("nearbysearch/json?")
    Observable<RestaurantsResult> getNearByRestaurant (@QueryMap Map<String, String> parameters);

    /**
     * Request HTTP in Json to have the Restaurant's Details
     * GET call for retrofit
     */
    @GET("details/json?fields=name,rating,formatted_address,formatted_phone_number,photos,website")
    Observable<Detail> getDetailRestaurant(@QueryMap Map<String, String> parameters);

}
