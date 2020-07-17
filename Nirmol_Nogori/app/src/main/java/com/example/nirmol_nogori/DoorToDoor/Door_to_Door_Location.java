
package com.example.nirmol_nogori.DoorToDoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nirmol_nogori.Adapter.LocationAdapter;
import com.example.nirmol_nogori.Model.Cleaner;
import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.NewsAndTrend.News_nd_Trend;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityDoorToDoorLocationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Door_to_Door_Location extends AppCompatActivity implements LocationAdapter.OnItemClickListener {
    private ActivityDoorToDoorLocationBinding binding;
    private static final String TAG = "Door_to_Door_Location";
    private RecyclerView rc_location;
    private DatabaseReference databaseReference;
    ArrayList<String> location = new ArrayList<String>();
    private LocationAdapter locationAdapter;
    private long lastclicktime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoorToDoorLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rc_location = findViewById(R.id.location_recyclerview);
        rc_location.setFitsSystemWindows(true);
        rc_location.setLayoutManager(new LinearLayoutManager(this));
        locationAdapter = new LocationAdapter(location, this);
        rc_location.setAdapter(locationAdapter);

        readLocation();


        binding.locationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchlocation(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void readLocation() {
        final ProgressDialog Dialog = new ProgressDialog(Door_to_Door_Location.this);
        Dialog.setMessage("Please wait ...... ");
        Dialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Location and Cleaner");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                location.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Dialog.dismiss();
                    String key = dataSnapshot1.getKey();
                    location.add(key);
                    Log.d(TAG, "Key:" + key);
                    Log.d(TAG, "Locations:" + location);
                }
                locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchlocation(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Location and Cleaner").orderByKey()
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                location.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "" + snapshot.getKey());
                    String key = snapshot.getKey();
                    location.add(key);

                }
                locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //for recycler views item
    @Override
    public void OnItemClick(String location_name) {
        if (SystemClock.elapsedRealtime() - lastclicktime < 1000) {
            return;
        }
        lastclicktime = SystemClock.elapsedRealtime();

        Intent intent = new Intent(Door_to_Door_Location.this, Door_to_Door_Service.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intent.putExtra("location_cleaner_admin_request", "location_cleaner_admin_request");
        }
        intent.putExtra("locationName", location_name);
        startActivity(intent);
        Log.d(TAG, "OnItemClick: clicked location name:" + location_name);
    }


}
