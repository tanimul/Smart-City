package com.example.nirmol_nogori.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nirmol_nogori.databinding.ActivityClenerInformationBinding;

public class Clener_information extends AppCompatActivity {
    private ActivityClenerInformationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityClenerInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
