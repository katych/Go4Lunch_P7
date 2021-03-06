package com.example.go4lunch.views.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkerHelper;
import com.example.go4lunch.model.Position;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.viewModel.ViewModel;
import com.example.go4lunch.views.activities.MainActivity;
import com.example.go4lunch.views.activities.RestaurantDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng lastPosition;
    private GoogleMap mGoogleMap;
    private ViewModel viewModel;
    private GoogleMapOptions mapOptions;

    private ListenerRegistration mListenerRegistration = null;
    private ArrayList<Worker> mWorkersArrayList;
    private static final float DEFAULT_ZOOM = 15;
    private static final String PREF_RADIUS = "radius_key";
    private static final String PREF_TYPE = "type_key";


    //constructor
    public MapsFragment() {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapOptions = new GoogleMapOptions()
                .mapType(GoogleMap.MAP_TYPE_NORMAL)
                .zoomControlsEnabled(true)
                .zoomGesturesEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        // Location Services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.requireContext(), R.raw.maps_style));
        final CollectionReference workersRef = WorkerHelper.getWorkersCollection();
        mListenerRegistration = workersRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mWorkersArrayList = new ArrayList<>();
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot data : Objects.requireNonNull(queryDocumentSnapshots).getDocuments()) {

                    if (data.get("placeId") != null) {
                        Worker workers = data.toObject(Worker.class);
                        mWorkersArrayList.add(workers);
                        Timber.i("snap workers : %s", mWorkersArrayList.size());
                    }
                }
            }
        });
        getLocationPermission();
        viewModel = ViewModelProviders.of(this.requireActivity()).get(ViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        //init map
        initMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListenerRegistration != null) {
            mListenerRegistration.remove();
        }
    }

    /**
     * get user location
     */
    private void getUserLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        // get the location phone
                        lastPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        Timber.i("LastLocation : %s", lastPosition.latitude);
                        //Update UI with information
                        updateUiMap(lastPosition);
                    }
                });
    }

    /**
     * check permission with dexter library
     */
    private void getLocationPermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //Get last location and update UI
                        getUserLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }


    /**
     * update UI with settings
     *
     * @param latLng user LatLng
     */
    private void updateUiMap(LatLng latLng) {
        //create user marker
        createUserMarker(ViewModel.generateUserPosition(latLng.latitude, latLng.longitude),
                mGoogleMap);
        //move camera to user position
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        //set my position and enable position button
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        //observe ViewModel restaurants data
        ViewModel.getAllRestaurants(latLng,
                sharedPreferences.getString(PREF_RADIUS, "2500"),
                sharedPreferences.getString(PREF_TYPE, "restaurant"))
                .observe(this.requireActivity(), this::generateRestaurantPosition);
    }


    /**
     * create custom marker
     */

    private void setMarkerPosition(Position position, GoogleMap mGoogleMap, int icon ) {

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(position.getLat(), position.getLong()))
                .title(position.getTitle())
                .icon(BitmapDescriptorFactory.fromResource(icon));
        Marker marker = mGoogleMap.addMarker(markerOptions);
        //Add tag to save restaurant placeId for earlier
        if ((position.getPlaceId() != null)) {
            marker.setTag(position.getPlaceId());
            marker.setTitle(position.getTitle());

        }
    }

    /**
     * create user marker
     */
   private void createUserMarker(Position position, GoogleMap mGoogleMap ) {

        setMarkerPosition(position, mGoogleMap, R.drawable.ic_my_position);

    }
    /**
     * create marker for all restaurant
     */
    private void createRestaurantsMarker(Position position, GoogleMap mGoogleMap) {
        if (position.isChosen()) {
            setMarkerPosition(position, mGoogleMap, R.drawable.ic_resto_green2);
        } else {
            setMarkerPosition(position, mGoogleMap, R.drawable.ic_resto_red2);
        }

    }

    /**
     * generate marker with restaurant list
     *
     * @param restaurants list
     */
     private void generateRestaurantPosition(ArrayList<Restaurant> restaurants) {
        List<Position> listPosition = viewModel.generatePositions(restaurants, mWorkersArrayList);
        for (Position position : listPosition) {
            createRestaurantsMarker(position, mGoogleMap);
            mGoogleMap.setOnMarkerClickListener(marker -> {
                    launchRestaurantDetail(marker);
                return true;
            });
        }
    }

    /**
     * launch detail restaurant page on marker click
     *
     * @param marker position of a restaurant
     */
    private void launchRestaurantDetail(Marker marker) {
        String ref = (String) marker.getTag();
        String name = marker.getTitle();
        Intent intent = new Intent(getContext(), RestaurantDetails.class);
        intent.putExtra("placeId", ref);
        intent.putExtra("restaurantName", name);
        startActivity(intent);
    }
    //init map
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);

        if (mapFragment == null) {
            //add option to the map
            mapFragment = SupportMapFragment.newInstance(mapOptions);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().replace(R.id.map_fragment, mapFragment).commit();
            }
        }
        mapFragment.getMapAsync(this);
    }





}