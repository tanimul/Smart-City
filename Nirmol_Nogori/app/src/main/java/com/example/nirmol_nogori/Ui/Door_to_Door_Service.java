
package com.example.nirmol_nogori.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nirmol_nogori.LocationAdapter;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityDoorToDoorServiceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Door_to_Door_Service extends AppCompatActivity {
    private ActivityDoorToDoorServiceBinding binding;
    private static final String TAG = "Door_to_Door_Service";
    private RecyclerView rc_location;
    private DatabaseReference databaseReference;
    ArrayList<String> location = new ArrayList<String>();
    private LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoorToDoorServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rc_location = findViewById(R.id.location_recyclerview);
        rc_location.setFitsSystemWindows(true);
        rc_location.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location and Cleaner");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String key = dataSnapshot1.getKey();
                    location.add(key);

                    Log.d(TAG, "Key:" + key);
                    Log.d(TAG, "Locations:" + location);
                }
                rc_location.setAdapter(new LocationAdapter(location));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ///problem
        locationAdapter.setOnItemClickListener(new LocationAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(Door_to_Door_Service.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //TOdo add searchview


}
