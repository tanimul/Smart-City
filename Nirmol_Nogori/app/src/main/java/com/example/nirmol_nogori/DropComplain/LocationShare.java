package com.example.nirmol_nogori.DropComplain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityLocationShareBinding;

public class LocationShare extends AppCompatActivity {
    private ActivityLocationShareBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLocationShareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
