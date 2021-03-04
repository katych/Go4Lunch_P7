package com.example.go4lunch.views.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.go4lunch.R;
import com.example.go4lunch.model.Position;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.viewModel.ViewModel;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.ArrayList;
import java.util.Objects;
import timber.log.Timber;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMapOptions mapOptions;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng lastPosition;
    private GoogleMap mGoogleMap;
    private ArrayList<Worker> mWorkersArrayList;
    private ListenerRegistration mListenerRegistration = null;
    private static final float DEFAULT_ZOOM = 15;
    private static final String PREF_ZOOM = "zoom_key";
    private static final String TAG = "MAP FRAGMENT " ;




    //constructor
    public MapsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        // Location Services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects.requireNonNull(this.getContext()), R.raw.maps_style));
        Timber.i("Map ready");
        getLocationPermission();
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
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getContext()));
        float zoom;
        switch (sharedPreferences.getString(PREF_ZOOM, "")) {
            case "High":
                zoom = 18f;
                break;
            case "Medium":
                zoom = 13f;
                break;
            case "Less":
                zoom = 9f;
                break;

            default:
                zoom = DEFAULT_ZOOM;
        }
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //set my position and enable position button
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        //observe ViewModel restaurants data
    }


    /**
     * create custom marker
     */

    private void setMarkerPosition(Position position, GoogleMap map, int icon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(position.getLat(), position.getLong()))
                .title(position.getTitle())
                .icon(BitmapDescriptorFactory.fromResource(icon));
        Marker marker = map.addMarker(markerOptions);
        //Add tag to save restaurant placeId for earlier
        if ((position.getPlaceId() != null)) {
            marker.setTag(position.getPlaceId());
            marker.setTitle(position.getTitle());
        }
    }

    /**
     * create user marker
     */
    private void createUserMarker(Position position, GoogleMap map) {
        setMarkerPosition(position, map, R.drawable.ic_marquer);
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