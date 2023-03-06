package com.example.nirmol_nogori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nirmol_nogori.Ui.Home_Menu;
import com.example.nirmol_nogori.databinding.ActivityThanksCreditBinding;

public class ThanksCredit extends AppCompatActivity {
    private ActivityThanksCreditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThanksCreditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backfromthankscredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThanksCredit.this, Home_Menu.class));
                finish();
            }
        });

    }
}
