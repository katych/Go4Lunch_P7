package com.example.go4lunch.apiRestaurantPlaces;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.model.DetailRestaurant;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.pojos.RestaurantsResult;
import com.example.go4lunch.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;


@SuppressWarnings("unused")
public class RepositoryRestaurantList implements NearbyPlaces {

    //FIELDS
    private MutableLiveData<ArrayList<Restaurant>> mRestaurantList;
    private MutableLiveData<DetailRestaurant> mDetailRestaurantLiveData;
    private Disposable disposable;
    public static final String mapKey= "AIzaSyCj9D_m5ZrbeSO_PipkQv7K8k5DdqUhuTk";

    //Implementation interface method
    @Override
    public MutableLiveData<ArrayList<Restaurant>> configureRetrofitCall(LatLng latLng, String radius, String type) {
        mRestaurantList = new MutableLiveData<>();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("location", latLng.latitude + "," + latLng.longitude);
        parameters.put("radius", radius);
        parameters.put("type", type);
        parameters.put("key", mapKey);

        this.disposable = PlaceStream.streamGetNearByRestaurant(parameters)
                .subscribeWith(new DisposableObserver<RestaurantsResult>() {
                    @Override
                    public void onNext(RestaurantsResult restaurantsResult) {
                        if (restaurantsResult != null) {
                            mRestaurantList.setValue(Utils.restaurantResultToRestaurant(restaurantsResult));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(String.valueOf(R.string.error_stream), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Timber.i(String.valueOf(R.string.on_Complete_message));
                    }
                });

        return this.mRestaurantList;
    }

    @Override
    public MutableLiveData<DetailRestaurant> configureDetailRestaurant(String placeId) {
        return null;
    }


}
