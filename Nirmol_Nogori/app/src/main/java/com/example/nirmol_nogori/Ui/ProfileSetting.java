package com.example.nirmol_nogori.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityProfileSettingBinding;


//Todo setting profile from firebase or email
//Todo Insert profile picture in firebase
public class ProfileSetting extends AppCompatActivity {
    private ActivityProfileSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
