package com.example.nirmol_nogori.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.Ui.Home_Activity;
import com.example.nirmol_nogori.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

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
    }

    @Override
    public void onClick(View v) {
        if (v == binding.buttonLoginAsUser) {
            userLogin();
        } else if (v == binding.textViewForgotpass) {

            startActivity(new Intent(Login_User.this, Password_Reset_Activity.class));

        } else if (v == binding.textViewFromLoginForRegistration) {
            startActivity(new Intent(Login_User.this, Registration.class));
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
                                startActivity(new Intent(Login_User.this, Home_Activity.class));
                            } else {
                                Toast.makeText(Login_User.this, "login unsuccessfully", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        } catch (Exception e) {
            Toast.makeText(Login_User.this, "login unsuccessfully", Toast.LENGTH_SHORT).show();
        }

    }


    //Show hide password
    public void ShowHidePass(View view) {
        if (binding.edittextLoginPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            //Show Password
            binding.edittextLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.eyebutton.setImageResource(R.drawable.hide);
        } else {
            //Hide Password
            binding.edittextLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.eyebutton.setImageResource(R.drawable.ic_eyes);
        }
    }

}
