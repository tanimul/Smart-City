package com.example.nirmol_nogori.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityAdminHomeBinding;

public class AdminHome extends AppCompatActivity implements View.OnClickListener {
    private ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BTDoorToDoor.setOnClickListener(this);
        binding.BTNewsTrend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == binding.BTDoorToDoor) {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminHome.this, Door_to_Door_Admin.class));
        }
        if (v == binding.BTNewsTrend) {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminHome.this, News_nd_Trend_Admin.class));
        }
    }
}
