package com.example.nirmol_nogori.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityLoginBinding;

public class Login_Admin extends AppCompatActivity {
    private ActivityLoginBinding binding;

    //Todo admin login just firebase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //testing
        startActivity(new Intent(Login_Admin.this, AdminHome.class));
    }
}
