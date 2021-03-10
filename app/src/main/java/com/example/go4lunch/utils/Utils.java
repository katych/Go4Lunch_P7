package com.example.go4lunch.utils;
import android.view.View;
import android.widget.ImageView;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.pojos.RestaurantsResult;
import com.example.go4lunch.pojos.Result;
import java.util.ArrayList;
import java.util.List;

public abstract class Utils {


    /**
     * Create an arraylist of restaurant with the json result
     *
     * @param restaurantsResult json result
     * @return arrayList of restaurant
     */
    public static ArrayList<Restaurant> restaurantResultToRestaurant(RestaurantsResult restaurantsResult) {

        ArrayList<Restaurant> restaurantList = new ArrayList<>();

        for (int i = 0; i < restaurantsResult.getResults().size(); i++) {
            Result restaurant1 = restaurantsResult.getResults().get(i);

            Boolean openNow;
            if (restaurant1.getOpeningHours() == null) {
                openNow = false;
            } else {
                openNow = restaurant1.getOpeningHours().getOpenNow();
            }

            String photo;
            if (restaurant1.getPhotos() == null) {
                photo = "";
            } else {
                photo = restaurant1.getPhotos().get(0).getPhotoReference();
            }
            Restaurant restaurant = new Restaurant(
                    restaurant1.getGeometry().getLocation(),
                    restaurant1.getName(),
                    restaurant1.getVicinity(),
                    restaurant1.getPlaceId(),
                    openNow,
                    photo,
                    0,
                    0,
                    restaurant1.getRating()
            );
            restaurantList.add(restaurant);
        }
        return restaurantList;
    }

    /**
     * set worker choice on restaurant list
     *
     * @param restaurantList   restaurant list
     * @param workers workers list
     * @return arrayList
     */
    public static ArrayList<Restaurant> getChoiceRestaurants(List<Restaurant> restaurantList, ArrayList<Worker> workers) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        restaurants.clear();
        for (Restaurant r : restaurantList) {
            int worker = 0;
            for (Worker w : workers) {
                if (r.getPlaceId().equalsIgnoreCase(w.getPlaceId())) {
                    worker++;
                    r.setChoice(true);
                    r.setWorkers(worker);
                }
            }
            restaurants.add(r);
        }
        return restaurants;
    }
    /**
     * create method to attribute stars
     *
     * @param rating restaurant rating
     * @return int to attribute stars
     */
    public static int starsAccordingToRating(double rating) {
        if (rating == 0) {
            return 0;
        } else if (rating > 0 && rating <= 2) {
            return 1;
        } else if (rating > 2 && rating < 3.7) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * update star with rating
     *
     * @param rating rating
     * @param s1     star 1
     * @param s2     star 2
     * @param s3     star 3
     */
    public static void starsView(int rating, ImageView s1, ImageView s2, ImageView s3) {
        switch (rating) {
            case 0:
                s1.setVisibility(View.GONE);
                s2.setVisibility(View.GONE);
                s3.setVisibility(View.GONE);
                break;
            case 1:
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.GONE);
                s3.setVisibility(View.GONE);
                break;
            case 2:
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.VISIBLE);
                s3.setVisibility(View.GONE);
                break;
            case 3:
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.VISIBLE);
                s3.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * filter restaurant with rating stars
     *
     * @param restaurants list
     * @param filter      filter
     * @return arrayList of restaurant filtered
     */
    public static ArrayList<Restaurant> filterRestaurantList(ArrayList<Restaurant> restaurants, int filter) {
        ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            int rating = Utils.starsAccordingToRating(restaurant.getRating());
            if (rating == filter) {
                restaurantArrayList.add(restaurant);
            }
        }
        return restaurantArrayList;
    }




}
