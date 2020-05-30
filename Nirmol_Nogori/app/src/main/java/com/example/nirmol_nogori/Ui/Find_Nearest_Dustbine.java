package com.example.nirmol_nogori.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityFindNearestDustbineBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Find_Nearest_Dustbine extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Find_Nearest_Dustbine";
    private ActivityFindNearestDustbineBinding binding;
    private GoogleMap map;
    private static final float DEFAULT_ZOOM = 17f;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;
    private Handler mainHandler = new Handler();
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindNearestDustbineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //======================================================= maps =======================================================

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentformap);
        mapFragment.getMapAsync(Find_Nearest_Dustbine.this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Thread GetLastLocationThread = new Thread(new GetLocationRunable(fusedLocationClient));
        GetLastLocationThread.start();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);


    }


    //new thread with recursion for avoid null location after location setting
    class GetLocationRunable implements Runnable {
        FusedLocationProviderClient fusedLocationProviderClient;

        public GetLocationRunable(FusedLocationProviderClient fusedLocationProviderClient) {
            this.fusedLocationProviderClient = fusedLocationProviderClient;
        }

        @Override
        public void run() {
            getLastLocation(fusedLocationProviderClient);
        }

        private void getLastLocation(FusedLocationProviderClient flpc) {
            Log.d(TAG, "getLastLocation: called");
            flpc.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(final Location location) {
                            if (location == null) {
                                Log.d(TAG, "onSuccess: loqcation is null");
                                getLastLocation(fusedLocationClient);
                            } else {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        lastLocation = location;
                                        moveCamera(location, "getLastLocation");
                                        yourlocation(location, "getYourLocation");
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: error=" + e.getMessage());
                        }
                    });
        }
    }

    //move the camera where you are
    public void moveCamera(Location location, String caller) {
        Log.d(TAG, "moveCamera: called by " + caller);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
    }

    //set your location in starting point(textview)
    public void yourlocation(Location location, String caller) {
        Log.d(TAG, "getlocation: called by " + caller);

        Geocoder geocoder = new Geocoder(Find_Nearest_Dustbine.this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            binding.searchStartLocation.setText("" + addressList.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
