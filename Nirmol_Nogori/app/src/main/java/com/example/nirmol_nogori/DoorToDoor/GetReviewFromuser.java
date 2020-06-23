package com.example.nirmol_nogori.DoorToDoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nirmol_nogori.Model.Cleaner;
import com.example.nirmol_nogori.Model.Review;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.Ui.Home_Menu;
import com.example.nirmol_nogori.Ui.MainActivity;
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
    String username = "";
    String userimageurl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetReviewFromuserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "Cleaner Name:" + CleanerProfile.cleanername);

        cleanerinformation();
        userinformation();

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

    //user information
    private void userinformation() {
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("ddddd", "" + userid);
        final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                username = users.getFirst_name();
                userimageurl = users.getUser_image_url();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //Adding review
    private void reviewsubmit(final String reviewCleanerTXTName, final String reviewCleanerTXTArea, final String workdate,
                              final String reviewET, final String totalfairET, final String totalhourET, final float rating) {
        final ProgressDialog Dialog = new ProgressDialog(GetReviewFromuser.this);
        Dialog.setMessage("Feedback is being shared ...");
        Dialog.show();
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "" + userid);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Experience");

        final Review review = new Review(workdate, Integer.parseInt(totalhourET), Integer.parseInt(totalfairET), reviewET, rating,
                userid, reviewCleanerTXTName, reviewCleanerTXTArea
                , userimageurl, username);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(reviewCleanerTXTName).child(userid).setValue(review);
                startActivity(new Intent(GetReviewFromuser.this, Home_Menu.class));
                Dialog.dismiss();
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

                        month = month + 1;
                        String monthofyear = "" + month;
                        String dayofmonth = "" + dayOfMonth;
                        if (month < 10) {
                            monthofyear = "0" + month;
                        }
                        if (dayOfMonth < 10) {
                            dayofmonth = "0" + dayOfMonth;
                        }
                        binding.workdate.setText(year + "-" + monthofyear + "-" + dayofmonth);
                    }
                }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();
    }


    private void cleanerinformation() {
        final ProgressDialog Dialog = new ProgressDialog(GetReviewFromuser.this);
        Dialog.setMessage("Please wait ...... ");
        Dialog.show();
        Log.d(TAG, "User id:" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        String cleanerName = CleanerProfile.cleanername;
        String area = CleanerProfile.cleanerarea;


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Location and Cleaner")
                .child(area).child(cleanerName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
Dialog.dismiss();
                Cleaner cleaner = dataSnapshot.getValue(Cleaner.class);
                binding.reviewCleanerTXTName.setText(cleaner.getName());
                binding.reviewCleanerTXTArea.setText(cleaner.getLocation());
                Picasso.get().load(cleaner.getImageurl())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(binding.reviewTXTClenarPhoto);
                Log.d(TAG, "Cleaner name:" + cleaner.getName());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
