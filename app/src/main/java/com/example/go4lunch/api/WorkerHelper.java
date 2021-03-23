package com.example.go4lunch.api;

import com.example.go4lunch.model.Worker;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class WorkerHelper {


    //FIELD
    private static final String COLLECTION_NAME = "workers";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getWorkersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---
    public static Task<Void> createWorker( String name ,String image, String placeId , String restaurant ) {
        Worker workerToCreate = new Worker(name,image, placeId ,restaurant) ;
        return WorkerHelper.getWorkersCollection().document().set(workerToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getWorker(String uid){
        return WorkerHelper.getWorkersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateWorkerName(String username, String uid) {
        return WorkerHelper.getWorkersCollection().document(uid).update("nameWorker", username);
    }

    // --- DELETE ---

    public static Task<Void> deleteWorker(String uid) {
        return WorkerHelper.getWorkersCollection().document(uid).delete();
    }

    // -- GET ALL Workers --
    public static Query getAllWorkers() {
        return WorkerHelper.getWorkersCollection();
    }

    // --- UPDATE ---
    public static void updateRestaurantChoice(String uid, String restaurantName, String placeId) {
        Map<String, Object> data = new HashMap<>();
        data.put("placeId", placeId);
        data.put("restaurantChoose", restaurantName);
        WorkerHelper.getWorkersCollection().document(uid).update(data);
    }
}


