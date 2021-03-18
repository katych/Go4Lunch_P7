package com.example.go4lunch.apiRestaurantPlaces;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.model.DetailRestaurant;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.pojos.Detail;
import com.example.go4lunch.pojos.DetailsResult;
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

    //Implementation interface method
    @Override
    public MutableLiveData<ArrayList<Restaurant>> configureRetrofitCall(LatLng latLng, String radius, String type) {
        mRestaurantList = new MutableLiveData<>();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("location", latLng.latitude + "," + latLng.longitude);
        parameters.put("radius", radius);
        parameters.put("type", type);
        parameters.put("key", BuildConfig.google_maps_key);

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
        mDetailRestaurantLiveData = new MutableLiveData<>();
        Map<String, String> parameters = new HashMap<>();

        parameters.put("place_id", placeId);
        parameters.put("key", BuildConfig.google_maps_key);

        this.disposable = PlaceStream.streamGetDetailRestaurant(parameters)
                .subscribeWith(new DisposableObserver<Detail>() {
                    @Override
                    public void onNext(Detail detail) {
                        if (detail != null) {
                            DetailsResult detailsResult = detail.getResult();
                            if (detailsResult != null) {
                                String photo;
                                if (detailsResult.getPhotos() == null) {
                                    photo = "";
                                } else {
                                    photo = detailsResult.getPhotos().get(0).getPhotoReference();
                                }

                                DetailRestaurant restaurant = new DetailRestaurant(
                                        detailsResult.getFormattedAddress(),
                                        detailsResult.getFormattedPhoneNumber(),
                                        detailsResult.getName(),
                                        photo,
                                        (detailsResult.getRating() != null) ? detailsResult.getRating() : 0,
                                        detailsResult.getWebsite()
                                );
                                mDetailRestaurantLiveData.setValue(restaurant);
                            }
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

        return mDetailRestaurantLiveData;
    }


}
