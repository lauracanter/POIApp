package com.lauracanter.poiapp;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.LocaleDisplayNames;
import android.location.*;

import com.google.android.gms.location.LocationListener;

import android.location.Location;
import android.media.audiofx.Equalizer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.MapMaker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


/**
 * Created by Laura on 08/03/2018.
 */

public class HomePageActivity extends AppCompatActivity implements  OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity", COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION, FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1, ERROR_DIALOG_REQUEST = 9001;
    private static final float DEFAULT_ZOOM = 17f, PROXIMITY_RADIUS = 60;

    private ActionBarDrawerToggle mToggle;
    private GoogleMap mMap;
    private DrawerLayout drawerLayout;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastKnownLocation;

    private DatabaseReference mDatabaseReference;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private UserPreferences mUserPreferences;

    private ArrayList<String> mEntertainmentAndSitesGoogleKeywords, mFoodAndDrinkGoogleKeywords, mNonGoogleKeywords, mShoppingGoogleKeywords;
    private String currentUserId;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");

        mMap = googleMap;
        mMap.setMaxZoomPreference(DEFAULT_ZOOM);
        mMap.setMinZoomPreference(DEFAULT_ZOOM);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //Set up menu
        //Toolbar toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();

        currentUserId = currentUser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User Preferences").child(currentUserId);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d("POIApp", "No permission, onCreate stopped");
            return;
        }

        //set up location permissions
        getLocationPermission();

        //Retrieve user info and store in arrays
        populateUserPreferencesArrays();

        //Check Google Maps working ok
        if (isServicesOk()) {
            Log.d("POIApp", "onCreate: isServices ok is working");
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void populateUserPreferencesArrays() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUserPreferences = dataSnapshot.getValue(UserPreferences.class);

                //Populate arrays with user's keywords
                mEntertainmentAndSitesGoogleKeywords = mUserPreferences.getEntertainmentAndSitesGoogleKeywords();
                mFoodAndDrinkGoogleKeywords = mUserPreferences.getFoodAndDrinkGoogleKeywords();
                mNonGoogleKeywords = mUserPreferences.getNonGoogleKeywords();
                mShoppingGoogleKeywords = mUserPreferences.getShoppingGoogleKeywords();

                Log.d("POIApp", "ArrayOutput: " + mEntertainmentAndSitesGoogleKeywords.get(0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean isServicesOk() {
        Log.d("POIApp", "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomePageActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make map requests
            Log.d("POIApp", "isServicesOk: Google Play Service is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d("POIApp", "isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomePageActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else
            Toast.makeText(HomePageActivity.this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void initMap() {
        Log.d("POIApp", "initMap");
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(HomePageActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        Log.d("POIApp", "getLocationPermission fineLocation Granted");
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("POIApp", "getLocationPermission coaseLocationGranted");

                initMap();
            } else {
                Log.d("POIApp", "getLocationPermission else Accessed");
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            Log.d("POIApp", "getLocationPermission else Accessed");
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: called.");

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");

                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("POIApp", "onConnectionSuspended ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("POIApp", "onConnectionFailed ");
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("POIApp", location.toString());

        //Animate movement of user
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

        if (mLastKnownLocation == null) {
            mLastKnownLocation = location;
            generatePlacesFromArrays(location);
            Toast.makeText(HomePageActivity.this, "Showing nearby places", Toast.LENGTH_SHORT).show();
        }



        if (location.distanceTo(mLastKnownLocation) > 20) {
            mMap.clear();

            Log.d("POIApp", "Travelled over 20 meters");

            generatePlacesFromArrays(location);

            Toast.makeText(HomePageActivity.this, "Showing nearby places", Toast.LENGTH_SHORT).show();

            mLastKnownLocation = location;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private String getUrl(double latitude, double longitude, String placeType) {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+placeType);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=AIzaSyCr9IWRpDQiErgDY-HDnShGUXaAGm4GLRQ");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    private String getUrlKeyword(double latitude, double longitude, String placeType) {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&keyword="+placeType);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=AIzaSyCr9IWRpDQiErgDY-HDnShGUXaAGm4GLRQ");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    public void generatePlacesAccordingToTagword(Location location, String keyword)
    {
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        String url = getUrl(location.getLatitude(), location.getLongitude(), keyword);
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
    }

    public void generatePlacesAccordingToKeyword(Location location, String keyword)
    {
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        String url = getUrlKeyword(location.getLatitude(), location.getLongitude(), keyword);
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
    }

    public void generatePlacesFromArrays(Location location)
    {
        //Retrieves according to type
        for(int i = 0; i<mEntertainmentAndSitesGoogleKeywords.size();i++)
        {
            generatePlacesAccordingToTagword(location, mEntertainmentAndSitesGoogleKeywords.get(i));
        }

        //Retrieves according to type
        for(int i = 0; i<mFoodAndDrinkGoogleKeywords.size();i++)
        {
            generatePlacesAccordingToTagword(location, mFoodAndDrinkGoogleKeywords.get(i));
        }

        //Retrieves according to type
        for(int i = 0; i<mShoppingGoogleKeywords.size();i++)
        {
            generatePlacesAccordingToTagword(location, mShoppingGoogleKeywords.get(i));
        }

        //Different URL generated for Keywords
        for(int i = 0; i<mNonGoogleKeywords.size();i++)
        {
            generatePlacesAccordingToKeyword(location, mNonGoogleKeywords.get(i));
        }
    }
}

