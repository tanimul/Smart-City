package com.example.nirmol_nogori.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityFindNearestDustbineBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Find_Nearest_Dustbine extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Find_Nearest_Dustbine";
    private ActivityFindNearestDustbineBinding binding;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindNearestDustbineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentformap);
        mapFragment.getMapAsync(Find_Nearest_Dustbine.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng bdbasurhat = new LatLng(22.867805, 91.273210);
        map.addMarker(new MarkerOptions().position(bdbasurhat).title("Bashurhat"));
        map.moveCamera(CameraUpdateFactory.newLatLng(bdbasurhat));
    }
}
