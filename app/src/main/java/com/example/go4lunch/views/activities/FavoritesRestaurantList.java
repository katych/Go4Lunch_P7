package com.example.go4lunch.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.go4lunch.R;
import com.example.go4lunch.api.RepositoryFirebase;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.views.adapters.FavoritesRestaurantListAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;

import timber.log.Timber;

public class FavoritesRestaurantList extends BaseActivity implements FavoritesRestaurantListAdapter.favoritesClickListener{

    //FIELDS
    private FavoritesRestaurantListAdapter adapter;
    private ArrayList<Restaurant> mRestaurantFavoriteList = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView_favorite_restaurant)
    RecyclerView mRecyclerView;
    @Override
    public int getActivityLayout() {
        return R.layout.activity_favorites_restaurant;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getBaseContext()));

        this.configureToolBar(getString(R.string.favorites_restaurant));
        if (mToolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        initListAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates a MenuInflater to add the menu xml file into the Toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_toolbar) {
            Timber.i("Search");
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * get only user favorite restaurant
     */
    private void initListAdapter() {
        Query query = RepositoryFirebase.getQueryFavoritesRestaurant(mRestaurantFavoriteList,
                Objects.requireNonNull(getCurrentUser()).getDisplayName());

        FirestoreRecyclerOptions<Restaurant> options = new FirestoreRecyclerOptions.Builder<Restaurant>()
                .setQuery(query, Restaurant.class)
                .build();

        adapter = new FavoritesRestaurantListAdapter(options, this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickItemRestaurant(int position) {
        Intent intent = new Intent(this, RestaurantDetails.class);
        intent.putExtra("placeId", mRestaurantFavoriteList.get(position).getPlaceId());
        intent.putExtra("restaurantName", mRestaurantFavoriteList.get(position).getNameRestaurant());
        startActivity(intent);
    }
}
