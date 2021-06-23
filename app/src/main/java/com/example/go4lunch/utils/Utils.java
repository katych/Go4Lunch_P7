package com.example.go4lunch.utils;

import android.view.View;
import android.widget.ImageView;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.pojos.RestaurantsResult;
import com.example.go4lunch.pojos.Result;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class Utils {


    //Show Snack Bar with a message
    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
    /**
     * Create an arrayList of restaurant with the json result
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
        for (Restaurant restaurant : restaurantList) {
            int worker = 0;
            for (Worker w : workers) {
                if (restaurant.getPlaceId().equalsIgnoreCase(w.getPlaceId())) {
                    worker++;
                    restaurant.setChoice(true);
                    restaurant.setWorkers(worker);
                }
            }

            restaurants.add(restaurant);
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
     * Sort the restaurants according to their rating
     */
    public static void sortRatingReverse(List<Restaurant> restaurantList)
    {
        Collections.sort(restaurantList, (o1, o2) -> {
            Double o1Rating = o1.getRating();
            Double o2Rating = o2.getRating();
            return o1Rating.compareTo(o2Rating);
        });
        Collections.reverse(restaurantList);
    }
    /**
     * Sort the restaurants according to their name
     */
    public static void sortName(List<Restaurant> restaurantList)
    {
        Collections.sort(restaurantList, (o1, o2) -> {
            String o1Name = o1.getNameRestaurant();
            String o2Name = o2.getNameRestaurant();
            return o1Name.compareToIgnoreCase(o2Name);
        });
    }

    /**
     * Sort the restaurants according to their distance from the CurrentUser
     */
    public static void sortProximity(List<Restaurant> restaurantList)
    {
        Collections.sort(restaurantList, (o1, o2) -> {
            Integer o1DistanceCurrentUser = o1.getDistanceCurrentWorker();
            Integer o2DistanceCurrentUser = o2.getDistanceCurrentWorker();
            return o1DistanceCurrentUser.compareTo(o2DistanceCurrentUser);
        });
    }
}
