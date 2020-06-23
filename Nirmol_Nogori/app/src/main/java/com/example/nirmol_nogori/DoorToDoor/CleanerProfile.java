package com.example.nirmol_nogori.DoorToDoor;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.nirmol_nogori.Adapter.ReviewAdapter;
import com.example.nirmol_nogori.Model.Review;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.Ui.MainActivity;
import com.example.nirmol_nogori.databinding.ActivityCleanerProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CleanerProfile extends AppCompatActivity {
    private static final String TAG = "Cleaner_Profile";
    private ActivityCleanerProfileBinding binding;
    private ArrayList<Review> reviews = new ArrayList<Review>();
    public static String cleanername = "";
    ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCleanerProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Bundle cleanerinformation = getIntent().getExtras();
        if (cleanerinformation != null) {

            String cleaner_name = cleanerinformation.getString("cleaner_name");
            String cleaner_area = cleanerinformation.getString("cleaner_area");
            String cleaner_imageurl = cleanerinformation.getString("cleaner_imageurl");
            float cleaner_ratings = cleanerinformation.getFloat("cleaner_ratings");
            int cleaner_totalfair = cleanerinformation.getInt("cleaner_totalfair");
            int cleaner_totalhr = cleanerinformation.getInt("cleaner_totalhr");

            binding.cleanerTXTName.setText(cleaner_name);
            binding.cleanerTXTArea.setText(cleaner_area);
            binding.ratingBar.setRating(cleaner_ratings);
            binding.cleanerTXTTFairValue.setText(cleaner_totalfair + "tk/" + cleaner_totalhr + "hr");
            Picasso.get().load(cleaner_imageurl).
                    placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop().into(binding.clenarPhoto);
        }


        binding.cleanerProfileRecyclerview.setFitsSystemWindows(true);
        binding.cleanerProfileRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.cleanerProfileRecyclerview.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter(reviews, CleanerProfile.this);
        binding.cleanerProfileRecyclerview.setAdapter(reviewAdapter);

        String cleaner_name = cleanerinformation.getString("cleaner_name");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Experience").child(cleaner_name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    binding.cleanerTXTReview.setVisibility(View.VISIBLE);
                } else {
                    binding.cleanerTXTReview.setText("Rate & Review");
                    binding.cleanerTXTReview.setVisibility(View.VISIBLE);
                }
                reviews.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Review review = dataSnapshot1.getValue(Review.class);
                    reviews.add(review);
                    Log.d(TAG, "" + dataSnapshot1.getValue());
                    Log.d(TAG, "Reviewer name: " + review.getUsername());
                }
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        binding.cleanerCallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestcleaner(CleanerProfile.this, cleanerinformation.getString("cleaner_name"), cleanerinformation.getString("cleaner_phone"));
            }
        });

    }

    //Request Cleaner by Call
    private void requestcleaner(Context context, String name, String number) {
        MainActivity.CLEANER_REQUEST = true;
     //   cleanername=name;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "" + number));
        context.startActivity(intent);
        Log.d(TAG, "Cleaner name: +" + name + " Cleaner number: " + number + " CLEANER_REQUEST:" + MainActivity.CLEANER_REQUEST);

    }
}
