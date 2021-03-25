package com.example.go4lunch.views.fragment;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkerHelper;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.utils.Utils;
import com.example.go4lunch.viewModel.ViewModel;
import com.example.go4lunch.views.activities.RestaurantDetails;
import com.example.go4lunch.views.adapters.RestaurantListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.ArrayList;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;



public class ListRestaurant extends Fragment implements RestaurantListAdapter.onClickRestaurantItemListener {

    //FOR DESIGN
    @BindView(R.id.recyclerView_list_restaurant)
    RecyclerView mRecyclerView;

    //FOR DATA
    private ViewModel viewModel;
    private ArrayList<Restaurant> mRestaurantsList;
    private ArrayList<Worker> mWorkersArrayList;
    private FusedLocationProviderClient mFusedLocationClient;
    private ListenerRegistration mListenerRegistration = null;
    private ArrayList<Restaurant> mRestaurantsToDisplay = new ArrayList<>();
    private LatLng currentPosition;
    private RestaurantListAdapter adapter;



    //constant
    private static final String PREF_RADIUS = "radius_key";
    private static final String PREF_TYPE = "type_key";
    private static final String TAG = "LIST RESTAURANT";
    private final CollectionReference workersRef = WorkerHelper.getWorkersCollection();

    public ListRestaurant() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Location Services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
        viewModel = ViewModelProviders.of(Objects.requireNonNull(this.getActivity())).get(ViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_restaurant, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setHasFixedSize(true);
        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
        initRestaurantList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListenerRegistration != null) {
            mListenerRegistration.remove();
        }
    }

    /**
     * create a list of restaurant with settings and current ic_my_position
     */
    private void initRestaurantList() {
        workersRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mWorkersArrayList = new ArrayList<>();
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot data : Objects.requireNonNull(queryDocumentSnapshots).getDocuments()) {
                    if (data.get("placeId") != null) {
                        Worker workers = data.toObject(Worker.class);
                        mWorkersArrayList.add(workers);
                        Timber.i("snap workers List Restaurant : %s", mWorkersArrayList.size());
                    }
                }
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getContext()));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                    if (location != null) {
                        // get the location phone
                        currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        //observe restaurants data
                        viewModel.getAllRestaurants(currentPosition,
                                sharedPreferences.getString(PREF_RADIUS, "2500"),
                                sharedPreferences.getString(PREF_TYPE, "restaurant"))
                                .observe(Objects.requireNonNull(this.getActivity()), this::initListAdapter);

                    }
                });
    }

    /**
     * init adapter for RV
     *
     * @param restaurants list
     */
    private void initListAdapter(ArrayList<Restaurant> restaurants) {
        mRestaurantsToDisplay.addAll(getRestaurantFromJson(restaurants));
        adapter = new RestaurantListAdapter(mRestaurantsToDisplay, Glide.with(this), this);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * format restaurant list with distance and workers information choice
     *
     * @param restaurants list
     * @return restaurant list
     */
    private ArrayList<Restaurant> getRestaurantFromJson(ArrayList<Restaurant> restaurants) {
        mRestaurantsList = Utils.getChoiceRestaurants(restaurants, mWorkersArrayList);
        getDistanceFromMyPosition(mRestaurantsList);
        return mRestaurantsList;
    }


    @Override
    public void onClickRestaurantItem(int position) {
        Intent intent = new Intent(getContext(), RestaurantDetails.class);
        intent.putExtra("placeId", mRestaurantsList.get(position).getPlaceId());
        intent.putExtra("restaurantName", mRestaurantsList.get(position).getNameRestaurant());
        startActivity(intent);
    }

    /**
     * calculate distance between restaurant and current ic_my_position
     *
     * @param restaurants list
     */
    private void getDistanceFromMyPosition(ArrayList<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            float[] results = new float[1];
            Location.distanceBetween(currentPosition.latitude, currentPosition.longitude,
                    restaurant.getLocation().getLat(), restaurant.getLocation().getLng(), results);
            int distance = (int) results[0];
            restaurant.setDistanceCurrentWorker(distance);
        }
    }

    ////////////////////////////////////////// ON CLICK  ///////////////////////////////////////////

    @OnClick(R.id.fragment_list_restaurants_near_me_fab)
    void triProximity() {
        Utils.sortProximity(mRestaurantsToDisplay);
        this.adapter.notifyDataSetChanged();

    }

    @OnClick(R.id.fragment_list_restaurants_rating_fab)
    void triRate() {
        Utils.sortRatingReverse(mRestaurantsToDisplay);
        this.adapter.notifyDataSetChanged();
    }


    @OnClick(R.id.fragment_list_restaurants_name_fab)
    void triName() {
        Utils.sortName(mRestaurantsToDisplay);
        this.adapter.notifyDataSetChanged();

    }

}


