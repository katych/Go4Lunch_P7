package com.example.go4lunch.views.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.api.FavoriteRestaurantHelper;
import com.example.go4lunch.api.WorkerHelper;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.model.DetailRestaurant;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.utils.Utils;
import com.example.go4lunch.viewModel.ViewModel;
import com.example.go4lunch.views.adapters.DetailWorkerAdapter;
import com.example.go4lunch.views.adapters.ListWorkersAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class RestaurantDetails extends BaseActivity {

    //FIELDS

    @BindView(R.id.activity_restaurant_details)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbarDetails)
    Toolbar mToolbar;
    @BindView(R.id.imageRestaurant)
    ImageView mImageRestaurant;
    @BindView(R.id.nameRestaurant)
    TextView mRestaurantName;
    @BindView(R.id.restaurantAddress)
    TextView mRestaurantAddress;
    @BindView(R.id.textLike)
    TextView mTextLike;
    @BindView(R.id.noWorkersText)
    TextView mTextNoWorker;
    @BindView(R.id.star1)
    ImageView mRestaurantStar1;
    @BindView(R.id.star2)
    ImageView mRestaurantStar2;
    @BindView(R.id.star3)
    ImageView mRestaurantStar3;
    @BindView(R.id.call_image)
    ImageButton callPhone;
    @BindView(R.id.website)
    ImageButton websiteButton;
    @BindView(R.id.like)
    ImageButton likeButton;
    @BindView(R.id.favorite_restaurant)
    ImageButton favoriteButton;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.recyclerView_workers_restaurant_detail)
    RecyclerView mRecyclerView;
    private ListenerRegistration mListenerRegistration = null;
    private static final int PERMISSION_CALL = 100;
    private String nameRestaurant;
    private String phoneNumber;
    private String websiteUrl;
    private String placeId;
    private Query query;
    private ArrayList<Restaurant> mFavoriteRestaurantList;
    private Restaurant mFavoriteRestaurant;
    private boolean isLiked = false;
    private ArrayList<Worker> mWorkers;





    @Override
    public int getActivityLayout() {
        return R.layout.activity_restaurant_details;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        //Add toolbar and return arrow
        this.configureToolBar("");

        if (mToolbar != null) {
           Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        //get placeId and name extra intent
        placeId = getIntent().getStringExtra("placeId");
        nameRestaurant = getIntent().getStringExtra("restaurantName");

        query = WorkerHelper.getAllWorkers().whereEqualTo("nameWorker",
                Objects.requireNonNull(getCurrentUser()).getDisplayName());
         //ViewModel
        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getDetailRestaurant(placeId).observe(this, this::updateUi);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListenerRegistration != null) {
            mListenerRegistration.remove();
        }
    }
    @OnClick({R.id.floatingActionButton,
            R.id.like,
            R.id.favorite_restaurant
    })
    public void onClickFabButton(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                updateFab();
            case R.id.like:
                this.saveRestaurantToFavorite(mFavoriteRestaurant);
                break;
            case R.id.favorite_restaurant:
                this.deleteFavoriteRestaurant();
                break;
        }
    }
    /**
     * update UI with restaurant detail information
     *
     * @param detailRestaurant restaurant detail information
     */
    private void updateUi(DetailRestaurant detailRestaurant) {
        //listen change on worker list
        listenChangeWorkersList();
        //path for photo url
        photoPath(detailRestaurant.getPhotoReference());
        //set name and address
        setNameRestaurant(detailRestaurant.getName());
        mRestaurantAddress.setText(detailRestaurant.getFormatted_address());
        //stars method according to rating
        Utils.starsView(Utils.starsAccordingToRating(detailRestaurant.getRating()), mRestaurantStar1, mRestaurantStar2, mRestaurantStar3);
        // Call restaurant if possible
        callPhone.setOnClickListener(v -> setCallPhoneIfPossible(detailRestaurant.getFormatted_phone_number()));

        // go to website if there is one
        websiteButton.setOnClickListener(view -> {
            if (detailRestaurant.getWebsite() == null) {
                Toast.makeText(this, R.string.no_website_for_restaurant, Toast.LENGTH_SHORT).show();
            } else {
                websiteUrl = detailRestaurant.getWebsite();
                callWebsiteUrl();
            }
        });
        mFavoriteRestaurant = new Restaurant("", detailRestaurant.getName(), placeId, detailRestaurant.getFormatted_address(),
                detailRestaurant.getPhotoReference(), detailRestaurant.getRating());
        //configure click on like star
        if (mFavoriteRestaurantList != null) {
            for (Restaurant restaurantFavorite : mFavoriteRestaurantList) {
                if (restaurantFavorite.getPlaceId().equalsIgnoreCase(placeId)) {
                    isLiked = true;
                    updateButtonLike();
                }
            }
        }
        fabButtonColor();
    }


    /**
     * get photo path if able and show it in glide
     *
     * @param photoText photo path
     */
    private void photoPath(String photoText) {
        String path;
        if (photoText == null) {
            path = "https://www.chilhoweerv.com/storage/app/public/blog/noimage930.png";
        } else {
            path = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + photoText +
                    "&key=" + BuildConfig.google_maps_key;
        }

        Glide.with(this)
                .load(path)
                .apply(RequestOptions.centerCropTransform())
                .into(mImageRestaurant);
    }

    /**
     * Format text restaurant name if it's too long
     *
     * @param restaurantText text of restaurant name
     */
    private void setNameRestaurant(String restaurantText) {
        String name;
        if (restaurantText.length() > 23) {
            name = restaurantText.substring(0, 23) + " ...";
        } else {
            name = restaurantText;
        }
        mRestaurantName.setText(name);
    }

    /**
     * check if phone permission is able
     *
     * @param callPhoneNumber call number
     */
    private void setCallPhoneIfPossible(String callPhoneNumber) {
        if (callPhoneNumber == null) {
            Toast.makeText(this, R.string.no_telephone_number_for_this_restaurant, Toast.LENGTH_SHORT).show();
        } else {
            phoneNumber = callPhoneNumber;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_CALL);
        } else {
            callPhone();
        }
    }

    /**
     * intent to call
     */
    @SuppressLint("MissingPermission")
    private void callPhone() {
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        startActivity(intentCall);
    }

    /**
     * intent to go to website
     */
    private void callWebsiteUrl() {
        Intent intentWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
        startActivity(intentWebsite);
    }

    /**
     * save restaurant to favorite list
     *
     * @param restaurant Restaurant
     */
    private void saveRestaurantToFavorite(Restaurant restaurant) {
            FavoriteRestaurantHelper.createFavoriteRestaurant(Objects.requireNonNull(getCurrentUser()).getDisplayName(),
                    restaurant.getUid(), restaurant.getNameRestaurant(), restaurant.getPlaceId(),
                    restaurant.getAddress(), restaurant.getImage(),
                    restaurant.getRating()).addOnFailureListener(this.onFailureListener());
            Utils.showSnackBar(this.mCoordinatorLayout, getString(R.string.favorite_restaurant_message));
            isLiked = true;
            updateButtonLike();
            getFavoriteRestaurant();
    }




    /**
     * update restaurant choice when star button is clicked
     *
     * @param id        id of the restaurant
     * @param restaurantName restaurant name
     * @param placeId   placeID
     * @param message   message to show in SnackBar
     * @param isChosen  boolean to configure FAB
     */
    private void updateRestaurantChoice(String id, String restaurantName, String placeId, String message, boolean isChosen) {
        WorkerHelper.updateRestaurantChoice(id, restaurantName, placeId);
        Utils.showSnackBar(this.mCoordinatorLayout, message);
        mFloatingActionButton.setImageResource(isChosen
                ? R.drawable.ic_check_checked
                : R.drawable.ic_check_circle_black);

    }

    /**
     * update the FAB button when you click on
     */
    private void updateFab() {
        this.query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String id = document.getId();
                    if (placeId.equalsIgnoreCase(Objects.requireNonNull(document.get("placeId")).toString())) {
                        updateRestaurantChoice(id, "", "",
                                getString(R.string.you_haven_t_choice_restaurant), false);

                    } else {
                        updateRestaurantChoice(id, nameRestaurant,
                                placeId, getString(R.string.chosen_restaurant), true);

                    }
                }
            }
        });
    }


    /**
     * set the button like appearance
     */
    private void updateButtonLike() {
        if (isLiked) {
            favoriteButton.setVisibility(View.VISIBLE);
            mTextLike.setVisibility(View.VISIBLE);
            likeButton.setVisibility(View.GONE);
            mTextLike.setVisibility(View.GONE);
        } else {
            favoriteButton.setVisibility(View.GONE);
            mTextLike.setVisibility(View.GONE);
            likeButton.setVisibility(View.VISIBLE);
            mTextLike.setVisibility(View.VISIBLE);
        }
    }


    /**
     * set the color of FAB button
     */
    private void fabButtonColor() {
        this.query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    if (placeId.equalsIgnoreCase(Objects.requireNonNull(document.get("placeId")).toString())) {
                        mFloatingActionButton.setImageResource(R.drawable.ic_check_checked);
                    } else {
                        mFloatingActionButton.setImageResource(R.drawable.ic_check_circle_black);
                    }
                }
            }
        });
    }


    /**
     * create query to have restaurant favorite list
     */
   private void getFavoriteRestaurant() {
        final Query refRestaurant = FavoriteRestaurantHelper.
                getAllRestaurantsFromWorkers(Objects.requireNonNull(getCurrentUser()).getDisplayName());
        refRestaurant.get()
                .addOnCompleteListener(task -> {
                    mFavoriteRestaurantList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        restaurant.setUid(document.getId());
                        Timber.d("restaurant uid = %s", restaurant.getUid());
                        mFavoriteRestaurantList.add(restaurant);
                    }
                    Timber.d("restaurantFavorite list : %s", mFavoriteRestaurantList.size());
                });
    }


    /**
     * method to delete restaurant if it's in favorite list
     */
    private void deleteFavoriteRestaurant() {
        getFavoriteRestaurant();
        for (Restaurant restaurantFavorite : mFavoriteRestaurantList) {
            if (restaurantFavorite.getPlaceId().equalsIgnoreCase(placeId)) {
                deleteRestaurantToFavorite(restaurantFavorite.getUid());
            }
        }
    }


    /**
     * delete restaurant to favorite list
     *
     * @param uid restaurant id
     */
    private void deleteRestaurantToFavorite(String uid) {
        FavoriteRestaurantHelper.deleteRestaurant(Objects.requireNonNull(getCurrentUser()).getDisplayName(), uid);
        Utils.showSnackBar(this.mCoordinatorLayout, getResources().getString(R.string.restaurant_delete));
        isLiked = false;
        updateButtonLike();
    }
    /**
     * Listen the modification on workers list
     */
    private void listenChangeWorkersList() {
        final CollectionReference workersRef = WorkerHelper.getWorkersCollection();
        mListenerRegistration = workersRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mWorkers = new ArrayList<>();
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot data : Objects.requireNonNull(queryDocumentSnapshots).getDocuments()) {

                    if (data.get("placeId") != null && Objects.requireNonNull(data.get("placeId")).toString().equals(placeId)) {

                        Worker workers = data.toObject(Worker.class);
                        mWorkers.add(workers);
                        Timber.i("snap workers : %s", mWorkers.size());
                    }
                }
            }
            initAdapter(mWorkers);
        });
    }

    /**
     * init adapter with list of workers
     *
     * @param workers list
     */
    private void initAdapter(ArrayList<Worker> workers) {
        if (workers.isEmpty()){
            mTextNoWorker.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else{
            DetailWorkerAdapter adapter = new DetailWorkerAdapter(workers);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setVisibility(View.VISIBLE);
            mTextNoWorker.setVisibility(View.GONE);
        }
    }
}




