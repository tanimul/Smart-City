package com.example.nirmol_nogori.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirmol_nogori.Home_Menu;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityLoginBinding;
import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_User extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();

        //Underline some text
        binding.textViewForgotpass.setPaintFlags(binding.textViewForgotpass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.textViewFromLoginForRegistration.setPaintFlags(binding.textViewFromLoginForRegistration.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.textViewFromLoginNeedHelp.setPaintFlags(binding.textViewFromLoginNeedHelp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        binding.textViewFromLoginForRegistration.setOnClickListener(this);
        binding.buttonLoginAsUser.setOnClickListener(this);
        binding.textViewForgotpass.setOnClickListener(this);
        binding.textViewFromLoginNeedHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == binding.buttonLoginAsUser) {
            userLogin();
        } else if (v == binding.textViewForgotpass) {

            //Todo perform forgot password to create another method

        } else if (v == binding.textViewFromLoginForRegistration) {
            startActivity(new Intent(Login_User.this, Registration.class));
        } else {

            //Todo perform need help to create another method

        }

    }

    //firebase login for user
    private void userLogin() {
        String user_email = binding.edittextLoginEmail.getText().toString().trim();
        String user_password = binding.edittextLoginPassword.getText().toString().trim();
        try {
            mAuth.signInWithEmailAndPassword(user_email, user_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login_User.this, "login successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login_User.this, Home_Menu.class));
                            } else {
                                Toast.makeText(Login_User.this, "login unsuccessfully", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        } catch (Exception e) {
            Toast.makeText(Login_User.this, "login unsuccessfully", Toast.LENGTH_SHORT).show();
        }

    }
}
