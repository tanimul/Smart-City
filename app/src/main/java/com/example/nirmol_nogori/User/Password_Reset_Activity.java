package com.example.nirmol_nogori.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.nirmol_nogori.databinding.ActivityPasswordResetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Password_Reset_Activity extends AppCompatActivity {
    private ActivityPasswordResetBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = binding.etPasswordEmail.getText().toString().trim();

                if (useremail.equals("")) {
                    Toast.makeText(Password_Reset_Activity.this, "Please enter  your registered email ID", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Password_Reset_Activity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Password_Reset_Activity.this, Login_User.class));
                            } else {
                                Toast.makeText(Password_Reset_Activity.this, "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });
    }
}
