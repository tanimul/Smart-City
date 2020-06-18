package com.example.nirmol_nogori.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityLoginAdminBinding;
import com.example.nirmol_nogori.databinding.ActivityLoginBinding;

public class Login_Admin extends AppCompatActivity {
    private ActivityLoginAdminBinding binding;

    //Todo admin login just firebase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //testing
        startActivity(new Intent(Login_Admin.this, AdminHome.class));
    }


    //Show hide password
    public void ShowHidePassAdm(View view) {
        if (binding.edittextLoginPasswordAdmin.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            //Show Password
            binding.edittextLoginPasswordAdmin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.eyebuttonaadmin.setImageResource(R.drawable.hide);
        } else {
            //Hide Password
            binding.edittextLoginPasswordAdmin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.eyebuttonaadmin.setImageResource(R.drawable.ic_eyes);
        }
    }
}
