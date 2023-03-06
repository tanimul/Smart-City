package com.example.nirmol_nogori.FindNearestDustbine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.nirmol_nogori.Interface.TaskLoadedCallback;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityFindNearestDustbineBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Find_Nearest_Dustbine extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private static final String TAG = "Find_Nearest_Dustbine";
    private ActivityFindNearestDustbineBinding binding;
    private GoogleMap map;
    private static final float DEFAULT_ZOOM = 17f;
    private FusedLocationProviderClient fusedLocationClient;
    private Handler mainHandler = new Handler();
    private LocationCallback locationCallback;
    int PROXIMITY_RADIUS = 10000;
    private Location last_location;
    MarkerOptions placestart, placedestination;
    Polyline cur_polyline;

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();

                Geocoder geocoder = new Geocoder(Find_Nearest_Dustbine.this, Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(position.latitude, position.longitude, 1);
                    binding.searchDestinationLocation.setText("" + addressList.get(0).getAddressLine(0));

                    LatLng latLng = new LatLng(last_location.getLatitude(), last_location.getLongitude());
                    LatLng latLng1 = new LatLng(position.latitude, position.longitude);

                    placestart = new MarkerOptions().position(latLng).title("locationstart");
                    placedestination = new MarkerOptions().position(latLng1).title("locationdestination");

                    String url = getDestinaonURl(placestart.getPosition(), placedestination.getPosition(), "walking");
                    new FetchURL(Find_Nearest_Dustbine.this).execute(url, "walking");

//                    map.addPolyline(new PolylineOptions()
//                            .add(latLng, new LatLng(position.latitude, position.longitude))
//                            .width(5)
//                            .color(Color.RED));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

    }

    private String getDestinaonURl(LatLng start, LatLng destination, String directionmode) {
        String st_start = "origin=" + start.latitude + "," + start.longitude;
        String st_destination = "destination=" + destination.latitude + "," + destination.longitude;
        String mode = "mode=" + directionmode;
        String parameters = st_start + "&" + st_destination + "&" + mode;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyBoXosK6UDF9Em9Zay4d-lZKm2azxuTqJw";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (cur_polyline != null)
            cur_polyline.remove();
        cur_polyline = map.addPolyline((PolylineOptions) values[0]);
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
            if (ActivityCompat.checkSelfPermission(Find_Nearest_Dustbine.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(Find_Nearest_Dustbine.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
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
                                        last_location = location;
                                        //   moveCamera(location, "getLastLocation");
                                        yourlocation(location, "getYourLocation");
                                        showarroundustbine(location.getLatitude(), location.getLongitude());
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

//    //move the camera where you are
//    public void moveCamera(Location location, String caller) {
//        Log.d(TAG, "moveCamera: called by " + caller);
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
//    }


    //showing nearby school for testing purpose
    private void showarroundustbine(double latitude, double longitude) {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlaceData getNearbyPlacesData = new GetNearbyPlaceData();
        //testing purpose
        String school = "school";

        String url = getUrl(latitude, longitude, school);
        dataTransfer[0] = map;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
        // Toast.makeText(Find_Nearest_Dustbine.this, "Showing Nearby schools", Toast.LENGTH_SHORT).show();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyBt5IftoRXNGG2kqGb2aWSiZ8GlWrDTeMQ");

        Log.d(TAG, "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
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
