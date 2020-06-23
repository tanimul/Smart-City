package com.example.nirmol_nogori.DoorToDoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Door_to_Door_Service extends AppCompatActivity {
    private ActivityDoorToDoorServiceBinding binding;
    private static final String TAG = "Door_to_Door_Service";
    private ArrayList<Cleaner> cleaners = new ArrayList<Cleaner>();
    CleanerAdapter cleanerAdapter;
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
            binding.doorToDoorServiceToolbarText.setText(title);
            Log.d(TAG, "bundle not null:" + title);
        }


        binding.cleanerRecyclerview.setFitsSystemWindows(true);
        binding.cleanerRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.cleanerRecyclerview.setHasFixedSize(true);
        cleanerAdapter = new CleanerAdapter(cleaners);
        binding.cleanerRecyclerview.setAdapter(cleanerAdapter);

        readCleaners();

        binding.cleanerSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchcleaner(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void searchcleaner(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Location and Cleaner").child(binding.doorToDoorServiceToolbarText.getText().toString())
                .orderByChild("name")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cleaners.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "" + snapshot.getKey());
                    Cleaner user = snapshot.getValue(Cleaner.class);
                    cleaners.add(user);

                }
                cleanerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readCleaners() {
        final ProgressDialog Dialog = new ProgressDialog(Door_to_Door_Service.this);
        Dialog.setMessage("Please wait ...... ");
        Dialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location and Cleaner").child(title);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cleaners.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Dialog.dismiss();
                    Cleaner cleaner = dataSnapshot1.getValue(Cleaner.class);
                    cleaners.add(cleaner);
                    Log.d(TAG, dataSnapshot1.getKey() + "key:" + "value:" + cleaner.getName());
                }
                cleanerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
