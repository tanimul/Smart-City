package com.example.nirmol_nogori.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.nirmol_nogori.databinding.ActivityHomeBinding;

public class Home_Activity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private long backpressed;
    private Toast backtost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    //for Double press for Exit
    @Override
    public void onBackPressed() {

        if (backpressed + 2000 > System.currentTimeMillis()) {
            backtost.cancel();
            super.onBackPressed();
            return;
        } else {
            backtost = Toast.makeText(Home_Activity.this, "press BACK again to Exit", Toast.LENGTH_SHORT);
            backtost.show();
        }

        backpressed = System.currentTimeMillis();

    }
}
