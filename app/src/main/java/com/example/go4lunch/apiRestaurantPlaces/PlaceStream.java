package com.example.go4lunch.apiRestaurantPlaces;

import com.example.go4lunch.pojos.Detail;
import com.example.go4lunch.pojos.RestaurantsResult;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.QueryMap;


class PlaceStream {

  /**
     * create observable data for nearby restaurant
     *
     * @param parameters for search restaurants
     * @return Restaurant Result of the request
     */
    static Observable<RestaurantsResult> streamGetNearByRestaurant(@QueryMap Map<String, String> parameters) {
        RestaurantService restaurantService = APIClient.retrofitCall();
        return restaurantService.getNearByRestaurant(parameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    /**
     * create observable data for restaurant detail
     */
    static Observable<Detail> streamGetDetailRestaurant(@QueryMap Map<String, String> parameters) {
        RestaurantService restaurantService = APIClient.retrofitCall();
        return restaurantService.getDetailRestaurant(parameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

}
