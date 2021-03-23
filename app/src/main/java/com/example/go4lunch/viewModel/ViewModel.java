package com.example.go4lunch.viewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.apiRestaurantPlaces.RepositoryRestaurantList;
import com.example.go4lunch.model.DetailRestaurant;
import com.example.go4lunch.model.Position;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ViewModel extends androidx.lifecycle.ViewModel {

    //FIELD
    private static RepositoryRestaurantList mRepositoryRestaurantList = new RepositoryRestaurantList();

    /**
     * generate user ic_my_position
     *
     * @param lat latitude
     * @param lng longitude
     * @return Position
     */
    public static Position generateUserPosition(double lat, double lng) {
        return new Position("My ic_my_position",
                "",
                lat,
                lng
        );
    }

    /**
     * generate list Poi with restaurant list
     *
     * @param restaurants       list
     * @param mWorkersArrayList list workers
     * @return list of poi
     */
    public List<Position> generatePositions(ArrayList<Restaurant> restaurants, ArrayList<Worker> mWorkersArrayList) {
        List<Position> positionList = new ArrayList<>();
        List<Restaurant> restaurants1 = Utils.getChoiceRestaurants(restaurants, mWorkersArrayList);

        for (Restaurant restaurant : restaurants1) {
            Position p = new Position(
                    restaurant.getNameRestaurant(),
                    restaurant.getPlaceId(),
                    restaurant.getLocation().getLat(),
                    restaurant.getLocation().getLng()
            );

            if (restaurant.isChoice()) {
                p.setChosen(true);
            }

            positionList.add(p);
        }
        return positionList;
    }

    /**
     * retrofit call to get all restaurants
     *
     * @param latLng latlng
     * @param radius for nearbysearch
     * @param type   of search
     * @return MutableLiveData
     */
    public static MutableLiveData<ArrayList<Restaurant>> getAllRestaurants(LatLng latLng, String radius, String type) {
        return mRepositoryRestaurantList.configureRetrofitCall(latLng, radius, type);
    }
    /**
     * retrofit call to get detail restaurant
     *
     * @param placeId placeId
     * @return MutableLiveData
     */
    public MutableLiveData<DetailRestaurant> getDetailRestaurant(String placeId) {
        return mRepositoryRestaurantList.configureDetailRestaurant(placeId);
    }


}
