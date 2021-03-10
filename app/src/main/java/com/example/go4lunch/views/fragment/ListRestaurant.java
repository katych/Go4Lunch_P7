package com.example.go4lunch.views.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
    @BindView(R.id.star1)
    TextView star1;
    @BindView(R.id.star2)
    TextView star2;
    @BindView(R.id.star3)
    TextView star3;
    @BindView(R.id.no_star)
    TextView no_star;
    @BindView(R.id.no_filter)
    TextView no_filter;
    @BindView(R.id.no_restaurant_text)
    TextView no_restaurant;

    //FOR DATA
    private ViewModel viewModel;
    private ArrayList<Restaurant> mRestaurantsList;
    private ArrayList<Worker> mWorkersArrayList;
    private FusedLocationProviderClient mFusedLocationClient;
    private ListenerRegistration mListenerRegistration = null;
    private ArrayList<Restaurant> mRestaurantsToDisplay = new ArrayList<>();
    private LatLng currentPosition;


    //constant
    private static final String PREF_RADIUS = "radius_key";
    private static final String PREF_TYPE = "type_key";
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
        initRestaurantList();
        return view;

    }
    @OnClick({R.id.no_star,
            R.id.star1,
            R.id.star3,
            R.id.star2,
            R.id.no_filter})
    void onClickFilter(View view) {
        mRestaurantsToDisplay.clear();
        switch (view.getId()) {
            case R.id.no_star:
                mRestaurantsToDisplay.addAll(Utils.filterRestaurantList(mRestaurantsList, 0));
                setButtonChoiceChange(R.id.no_star);
                break;
            case R.id.star1:
                mRestaurantsToDisplay.addAll(Utils.filterRestaurantList(mRestaurantsList, 1));
                setButtonChoiceChange(R.id.star1);
                break;
            case R.id.star2:
                mRestaurantsToDisplay.addAll(Utils.filterRestaurantList(mRestaurantsList, 2));
                setButtonChoiceChange(R.id.star2);
                break;
            case R.id.star3:
                mRestaurantsToDisplay.addAll(Utils.filterRestaurantList(mRestaurantsList, 3));
                setButtonChoiceChange(R.id.star3);
                break;
            case R.id.no_filter:
                mRestaurantsToDisplay.addAll(mRestaurantsList);
                setButtonChoiceChange(R.id.no_filter);
                break;
        }
        if (mRestaurantsToDisplay.isEmpty()){
            no_restaurant.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else{
            Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
            no_restaurant.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
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
     * create a list of restaurant with settings and current position
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
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                    if (location != null) {
                        // get the location phone
                        currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        //observe restaurants data
                        viewModel.getAllRestaurants(currentPosition,
                                sharedPreferences.getString(PREF_RADIUS, "1500"),
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
        RestaurantListAdapter adapter = new RestaurantListAdapter(mRestaurantsToDisplay, Glide.with(this), this);
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
     * calculate distance between restaurant and current position
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

    private void setButtonChoiceChange(int textView){
        switch (textView){
            case R.id.no_star:
                no_star.setTextColor(getResources().getColor(R.color.colorAccent));
                star1.setTextColor(getResources().getColor(R.color.white_text));
                star2.setTextColor(getResources().getColor(R.color.white_text));
                star3.setTextColor(getResources().getColor(R.color.white_text));
                no_filter.setTextColor(getResources().getColor(R.color.white_text));
                break;
            case R.id.star1:
                star1.setTextColor(getResources().getColor(R.color.colorAccent));
                no_star.setTextColor(getResources().getColor(R.color.white_text));
                star2.setTextColor(getResources().getColor(R.color.white_text));
                star3.setTextColor(getResources().getColor(R.color.white_text));
                no_filter.setTextColor(getResources().getColor(R.color.white_text));
                break;
            case R.id.star2:
                star2.setTextColor(getResources().getColor(R.color.colorAccent));
                star1.setTextColor(getResources().getColor(R.color.white_text));
                no_star.setTextColor(getResources().getColor(R.color.white_text));
                star3.setTextColor(getResources().getColor(R.color.white_text));
                no_filter.setTextColor(getResources().getColor(R.color.white_text));
                break;
            case R.id.star3:
                star3.setTextColor(getResources().getColor(R.color.colorAccent));
                star1.setTextColor(getResources().getColor(R.color.white_text));
                star2.setTextColor(getResources().getColor(R.color.white_text));
                no_star.setTextColor(getResources().getColor(R.color.white_text));
                no_filter.setTextColor(getResources().getColor(R.color.white_text));
                break;
            case R.id.no_filter:
                no_filter.setTextColor(getResources().getColor(R.color.colorAccent));
                star1.setTextColor(getResources().getColor(R.color.white_text));
                star2.setTextColor(getResources().getColor(R.color.white_text));
                star3.setTextColor(getResources().getColor(R.color.white_text));
                no_star.setTextColor(getResources().getColor(R.color.white_text));
                break;

        }
    }


}


