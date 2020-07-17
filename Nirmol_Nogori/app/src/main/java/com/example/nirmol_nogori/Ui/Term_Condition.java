package com.example.nirmol_nogori.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nirmol_nogori.databinding.ActivityTermConditionBinding;

public class Term_Condition extends AppCompatActivity {
    private ActivityTermConditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTermConditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backfromtermcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Term_Condition.this, Home_Menu.class));
                finish();
            }
        });

    }
}
