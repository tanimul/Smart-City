package com.example.nirmol_nogori.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nirmol_nogori.Model.Admin;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityLoginAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Login_Admin extends AppCompatActivity {
    private ActivityLoginAdminBinding binding;
    DatabaseReference databaseReference;
    private static final String TAG = "Admin_Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //testing
        ///startActivity(new Intent(Login_Admin.this, AdminHome.class));
        binding.buttonLoginAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

    }

    private void validation() {
        final String email = binding.edittextLoginEmailAdmin.getText().toString().trim();
        final String password = binding.edittextLoginPasswordAdmin.getText().toString().trim();

        databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "total children: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d(TAG, "" + dataSnapshot1.getValue());
                    Admin admin = dataSnapshot1.getValue(Admin.class);

                    if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
                        Toast.makeText(Login_Admin.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "wow");
                        Intent intent = new Intent(Login_Admin.this, AdminHome.class);
                        intent.putExtra("adminid", "" + admin.getAdminid());
                        intent.putExtra("name", "" + admin.getName());
                        intent.putExtra("email", "" + admin.getEmail());
                        intent.putExtra("phoneno", "" + admin.getMobile());
                        intent.putExtra("imageurl", "" + admin.getImageurl());
                        startActivity(intent);
                        finish();
                        break;

                    } else {
                        Log.d(TAG, "wrong");
                        Toast.makeText(Login_Admin.this, "Login UnSuccessfully", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //Show hide password
    public void ShowHidePassAdm(View view) {
        if (binding.edittextLoginPasswordAdmin.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            //Show Password
            binding.edittextLoginPasswordAdmin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.eyebuttonaadmin.setImageResource(R.drawable.ic_eyes);
            binding.edittextLoginPasswordAdmin.setHint("");
        } else {
            //Hide Password
            binding.edittextLoginPasswordAdmin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.eyebuttonaadmin.setImageResource(R.drawable.hide);
            binding.edittextLoginPasswordAdmin.setHint("********");
        }
    }
}
