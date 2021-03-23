package com.example.go4lunch.api;

import com.example.go4lunch.model.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;


public class FavoriteRestaurantHelper {
     //filed
    private static final String COLLECTION_NAME = "favorite_restaurant" ;


    // --- CREATE ---

    public static Task<DocumentReference> createFavoriteRestaurant(String user, String uid, String name, String placeId,
            String address, String photoReference, double rating) {
        Restaurant restaurantToCreate = new Restaurant(uid, name, placeId, address, photoReference, rating);
        //Store RestaurantFavorite to FireStore
        return WorkerHelper.getWorkersCollection()
                .document(user)
                .collection(COLLECTION_NAME)
                .add(restaurantToCreate);
    }



    public static Query getAllRestaurantsFromWorkers(String name) {
        return WorkerHelper.getWorkersCollection()
                .document(name)
                .collection(COLLECTION_NAME);
    }

    // --- DELETE ---

    public static void deleteRestaurant(String user, String placeId) {
        WorkerHelper.getWorkersCollection()
                .document(user)
                .collection(COLLECTION_NAME)
                .document(placeId)
                .delete();
    }


}
