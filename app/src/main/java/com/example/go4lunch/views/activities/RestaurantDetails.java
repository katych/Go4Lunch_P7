package com.example.go4lunch.views.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.model.DetailRestaurant;
import com.example.go4lunch.utils.Utils;
import com.example.go4lunch.viewModel.ViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;


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
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.recyclerView_workers_restaurant_detail)
    RecyclerView mRecyclerView;
    private ListenerRegistration mListenerRegistration = null;
    private static final int PERMISSION_CALL = 100;
    private  String nameRestaurant;
    private String phoneNumber;
    private String websiteUrl;
    private String placeId;



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
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //get placeId and name extra intent
        placeId = getIntent().getStringExtra("placeId");
        nameRestaurant = getIntent().getStringExtra("restaurantName");

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

    /**
     * update UI with restaurant detail information
     *
     * @param detailRestaurant restaurant detail information
     */
    private void updateUi(DetailRestaurant detailRestaurant) {
        //listen change on worker list
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

}




