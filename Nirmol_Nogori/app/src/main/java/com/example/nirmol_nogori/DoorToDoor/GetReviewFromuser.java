package com.example.nirmol_nogori.DoorToDoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nirmol_nogori.Model.Cleaner;
import com.example.nirmol_nogori.Model.Review;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityGetReviewFromuserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class GetReviewFromuser extends AppCompatActivity {
    private ActivityGetReviewFromuserBinding binding;
    private DatePickerDialog datePickerDialog;
    private static final String TAG = "Get_Review_From_User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetReviewFromuserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cleanerinformation();

        binding.workdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate();
            }
        });


        binding.reviewUserSubmitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewsubmit(binding.reviewCleanerTXTName.getText().toString(), binding.reviewCleanerTXTArea.getText().toString()
                        , binding.workdate.getText().toString(), binding.reviewET.getText().toString(),
                        binding.totalfairET.getText().toString(), binding.totalhourET.getText().toString(), binding.reviewRatingBar.getRating());
            }
        });


    }

    private void reviewsubmit(final String reviewCleanerTXTName, final String reviewCleanerTXTArea, final String workdate,
                              final String reviewET, final String totalfairET, final String totalhourET, final float rating) {

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("ddddd", "" + userid);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Experience");


//        final Review review = new Review(workdate, Integer.parseInt(totalhourET), Integer.parseInt(totalfairET), reviewET, rating,
//                userid, reviewCleanerTXTName, reviewCleanerTXTArea
//                , null, "bisad");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChild(reviewCleanerTXTName)) {
//                    Log.d(TAG, "available");
//                    databaseReference.child(reviewCleanerTXTName).child(userid).setValue(review);
//                } else {
//                    Log.d(TAG, "not available");
//                    databaseReference.child(reviewCleanerTXTName).setValue(review);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getdate() {

        Calendar mcurrentDate = Calendar.getInstance();
        int currentDay = 0;
        int currentYear = 0;
        int currentMonth = 0;
        if (currentYear == 0 || currentMonth == 0 || currentDay == 0) {

            currentYear = mcurrentDate.get(Calendar.YEAR);

            currentMonth = mcurrentDate.get(Calendar.MONTH);
            currentDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        }
        datePickerDialog = new DatePickerDialog(GetReviewFromuser.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        binding.workdate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();
    }


    private void cleanerinformation() {
        //testing
        //   String usrid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String usrid = "HMf7C5z3erdMgqxseyz0alneq973";
        // String cleanerName=CleanerProfile.cleanername;
        String cleanerName = "cerag ali";
        String area = "kalabagan";
        // Log.d("dddd", "" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Location and Cleaner")
                .child(area).child(cleanerName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Cleaner review = dataSnapshot.getValue(Cleaner.class);
                binding.reviewCleanerTXTName.setText(review.getName());
                binding.reviewCleanerTXTArea.setText(review.getLocation());
                Picasso.get().load(review.getImageurl())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(binding.reviewTXTClenarPhoto);
                Log.d("ddddd", "" + review.getName());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
