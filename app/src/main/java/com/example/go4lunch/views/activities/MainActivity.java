package com.example.go4lunch.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkerHelper;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.views.fragment.ListRestaurant;
import com.example.go4lunch.views.fragment.MapsFragment;
import com.example.go4lunch.views.fragment.WorkmatesList;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.view.GravityCompat.START;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;
    private Fragment mFragment;
    private FirebaseUser user;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        user = this.getCurrentUser();
        if (mFragment == null) {
            mFragment = new MapsFragment();
        }
        user = this.getCurrentUser();
        configureFragment(mFragment);
        this.configureToolBar(getString(R.string.I_m_hungry));

        configureDrawerLayout();
        configureNavigationView();
        configureBottomNavigation();

        // Initialize the SDK for autocomplete
        Places.initialize(getApplicationContext(), BuildConfig.google_maps_key);
    }

    /**
     * config drawerLayout
     */
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /**
     * Configuration Navigation View
     */
    public void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        this.updateNavigationHeader();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.drawer_yourLunch:
                showMyRestaurantChoice();
                Toast.makeText(this, "your lunch", Toast.LENGTH_LONG).show();
                break;
            case R.id.drawer_favorite_restaurant:
                this.showMyFavoriteRestaurant();
                Toast.makeText(this, "favorite restaurant", Toast.LENGTH_LONG).show();

                break;
            case R.id.drawer_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_LONG).show();
                break;
            case R.id.drawer_logOut:
                createAndShowPopUpLogOut();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    public void configureBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mapView:
                    this.mFragment = new MapsFragment();
                    configureFragment(mFragment);
                    toolbar.setTitle(getResources().getString(R.string.I_m_hungry));
                    break;

                case R.id.listView:
                    this.mFragment = new ListRestaurant();
                    configureFragment(mFragment);
                    toolbar.setTitle(getResources().getString(R.string.I_m_hungry));
                    break;

                case R.id.workmates:
                    this.mFragment = new WorkmatesList();
                    configureFragment(mFragment);
                    toolbar.setTitle(getResources().getString(R.string.workers_toolbar));
                    break;
            }
            // Closes the DrawerNavigationView when the user click on an item
            if (this.drawerLayout.isDrawerOpen(START)) {
                this.drawerLayout.closeDrawer(START);
            }
            return true;
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates a MenuInflater to add the menu xml file into the Toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        configureDrawerLayout();
        return true;
    }

    private void createAndShowPopUpLogOut()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.main_activity_pop_up_log_out_title));
        builder.setMessage(getResources().getString(R.string.main_activity_pop_up_log_out_message));
        builder.setPositiveButton(getResources().getString(R.string.main_activity_pop_up_yes), (dialogInterface, i) -> signOutCurrentUser());
        builder.setNegativeButton(getResources().getString(R.string.main_activity_pop_up_no), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Signs out the current user of Firebase
     */
    private void signOutCurrentUser() {
            FirebaseAuth.getInstance().signOut();
            startAuthenticationActivity();
            finishAffinity();
        }

    // Intent used for navigation item
    private void startAuthenticationActivity() {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
    }
    private void showMyFavoriteRestaurant() {
        Intent intent = new Intent(this, FavoritesRestaurant.class);
        startActivity(intent);
    }

    /**
     * create new fragment
     *
     * @param fragment to configure
     */
    private void configureFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // fragment open first after permission granted
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(fragment).onActivityResult(requestCode, resultCode, data);

        //  go to detail page when we click on result search
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Intent intent = new Intent(this, RestaurantDetails.class);
                intent.putExtra("placeId", place.getId());
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_query), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_toolbar) {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS);
            // Define the region
            RectangularBounds bounds = RectangularBounds.newInstance(
                    new LatLng(47.2184, -1.5536),
                    new LatLng(47.2205, -1.5435));

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .setLocationBias(bounds)
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .build(MainActivity.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Update header with user information
     */
    private void updateNavigationHeader() {
        final View headerNav = navigationView.getHeaderView(0);

        //XML id for update data
        ImageView imageViewNav = headerNav.findViewById(R.id.imageProfileHeader);
        TextView textViewNavName = headerNav.findViewById(R.id.nameProfileHeader);
        TextView textViewNavMail = headerNav.findViewById(R.id.emailProfileHeader);

        if (user != null) {
            // ImageView: User image
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .circleCrop()
                        .into(imageViewNav);
            }

            // TextView: Username and email
            final String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.no_name_found) :
                    user.getDisplayName();

            final String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.no_mail_found) :
                    user.getEmail();

            textViewNavName.setText(username);
            textViewNavMail.setText(email);
        }
    }
    /**
     * get a user query to show if the user has chosen a restaurant and redirect if able
     */
    private void showMyRestaurantChoice() {
        Query query = WorkerHelper.getAllWorkers().whereEqualTo("workerName",
                Objects.requireNonNull(getCurrentUser()).getDisplayName());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    if (!Objects.equals(document.get("placeId"), "")) {
                        Intent intent = new Intent(this.getBaseContext(), RestaurantDetails.class);
                        intent.putExtra("placeId", Objects.requireNonNull(document.get("placeId")).toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_choice_restaurant_workers), Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

}