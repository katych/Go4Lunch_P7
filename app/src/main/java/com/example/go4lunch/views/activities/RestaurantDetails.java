package com.example.go4lunch.views.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
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

    }


    }
