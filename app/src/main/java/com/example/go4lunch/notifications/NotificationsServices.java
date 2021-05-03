package com.example.go4lunch.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkerHelper;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.views.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.ArrayList;
import java.util.Objects;

public class NotificationsServices extends FirebaseMessagingService {


    private static final String PREF_NOTIFICATION = "notification_firebase";
    private String  restaurant;
    private ArrayList<Worker> mWorkers;



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //get restaurant name
        getRestaurantUser();
        //get workers list
        mWorkers = getRestaurantUserAndWorkersAdd();
        //put Thread on sleep for 2 sec to make sure that the query are finish
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // send message if able
        if(remoteMessage.getNotification()!= null &&
                sharedPreferences.getBoolean(PREF_NOTIFICATION,true)
                && restaurant !=null){

            // Get message sent by Firebase
            String message = remoteMessage.getNotification().getBody();
             message+= " " + "it's time to go to" + " " + restaurant + " " + "with";
             String message2 = createMessageNotification();

             // Show message
            showNotifications(message,message2);
        }
    }
private void showNotifications(String body,String message){

    //Create an Intent that will be shown when user will click on the Notification
    Intent intent = new Intent(this, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(),
            0, intent, PendingIntent.FLAG_ONE_SHOT);

    //Add the Notification to the Notification Manager and show it.
    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

    // Create a Channel (Android 8)
    String channelId = getString(R.string.default_notification_channel_id);

    //Create a Style for the Notification
    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    inboxStyle.setBigContentTitle(getString(R.string.app_name));
    inboxStyle.addLine(body).addLine(message);

    NotificationCompat.Builder builder =  new NotificationCompat.Builder(getApplicationContext(),channelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_orange_bowl)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setStyle(inboxStyle);


    //Show notification
    notificationManager.notify(1,builder.build());


    // Support Version >= Android 8
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        CharSequence channelName = getResources().getString(R.string.message_from_go4lunch);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
        Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
    }
}
    /**
     * get restaurant choice
     */
    private void getRestaurantUser(){
        Query query = WorkerHelper.getAllWorkers().whereEqualTo("nameWorker",
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        query.get().addOnCompleteListener(task -> {
            restaurant = "";
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                    restaurant = Objects.requireNonNull(documentSnapshot.get("restaurantName")).toString();

                }
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * get ArrayList of workers who are chosen the same restaurant
     * @return Workers ArrayList
     */
    private ArrayList<Worker> getRestaurantUserAndWorkersAdd(){
        Query query = WorkerHelper.getAllWorkers();
        ArrayList<Worker> workers = new ArrayList<>();
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot data : Objects.requireNonNull(task.getResult())) {
                    if (Objects.requireNonNull(data.get("restaurantName")).toString().equals(restaurant)) {
                        Worker worker = data.toObject(Worker.class);
                        workers.add(worker);
                    }
                }
            }
        });
        return workers;
    }

    /**
     * Create a string with the workers name
     * @return String message
     */
    private String createMessageNotification(){
        StringBuilder message = new StringBuilder();
        for (int i = 0; i< mWorkers.size(); i++){
            if (!mWorkers.get(i).getNameWorker().
                    equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName())){
                message.append(mWorkers.get(i).getNameWorker());
                if (i < mWorkers.size()-1){
                    message.append(" , ");
                }
            }

        }
        return message.toString();
    }
}
