package com.example.go4lunch.api;

import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Worker;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Objects;
import timber.log.Timber;

public abstract class RepositoryFirebase {

    /**
     * create a query for workers BDD
     *
     * @param mWorkers list of workers
     * @return query for Firebase recyclerView options
     */
    public static Query getQueryWorkers(List<Worker> mWorkers) {
        Query query = WorkerHelper.getWorkersCollection().orderBy("restaurantChoose", Query.Direction.DESCENDING);
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Worker worker = document.toObject(Worker.class);
                            mWorkers.add(worker);
                            Timber.d(document.getId() + " => " + document.getData());
                        }
                    } else {
                        Timber.w(String.valueOf(R.string.error_query), Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
        return query;
    }

    /**
     * create a query for favorite restaurant BDD
     *
     * @param favorites list of restaurant
     * @param user      user logged
     * @return query for firebase RV
     */
    public static Query getQueryFavoritesRestaurant(List<Restaurant> favorites, String user) {
        Query query = FavoriteRestaurantHelper.getAllRestaurantsFromWorkers(user);
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Restaurant restaurantFavorite = document.toObject(Restaurant.class);
                            favorites.add(restaurantFavorite);
                        }
                    } else {
                        Timber.w(String.valueOf(R.string.error_query), Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
        return query;
    }

}
