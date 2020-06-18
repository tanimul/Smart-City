package com.example.nirmol_nogori.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.nirmol_nogori.Adapter.CleanerAdapter;
import com.example.nirmol_nogori.Model.Cleaner;
import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.Model.Review;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityDoorToDoorServiceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Door_to_Door_Service extends AppCompatActivity {
    private ActivityDoorToDoorServiceBinding binding;
    private static final String TAG = "Door_to_Door_Service";
    private ArrayList<Cleaner> cleaners = new ArrayList<Cleaner>();
    private ArrayList<Review> reviews = new ArrayList<Review>();
    CleanerAdapter CleanerAdapter;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoorToDoorServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("locationName");
            binding.doorToDoorServiceToolbar.setTitle(title);
            Log.d(TAG, "bundle not null:" + title);
        }


        binding.cleanerRecyclerview.setFitsSystemWindows(true);
        binding.cleanerRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.cleanerRecyclerview.setHasFixedSize(true);
        CleanerAdapter = new CleanerAdapter(cleaners);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location and Cleaner").child(title);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cleaners.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Cleaner cleaner = dataSnapshot1.getValue(Cleaner.class);
                    cleaners.add(cleaner);
                    binding.cleanerRecyclerview.setAdapter(CleanerAdapter);
                    Log.d(TAG, dataSnapshot1.getKey() + "key:" + "value:" + cleaner.getName());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //just for testing
        String alom = "alom";
        String path = "jvkOpv04aAc73y9DFOJbXRFVUxk2";
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Experience").child(alom).child(path);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Review review = dataSnapshot.getValue(Review.class);

                Log.d("ddddd", "" + review.getCleanername());
                Log.d("ddddd", "" + review.getReview());
                Log.d("ddddd", "" + review.getRating());
                Log.d("ddddd", "" + review.getLocation());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
