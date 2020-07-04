package com.example.nirmol_nogori.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivitySettingBinding;

public class Setting extends AppCompatActivity {
    private ActivitySettingBinding binding;
    public static boolean switchonoff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //check & show autoupdate on or off
        if (switchonoff == true) {
            binding.autoupdateswitch.setChecked(true);

        } else {
            binding.autoupdateswitch.setChecked(false);
        }




        //Todo create auto update mode
        //autoupdatesetting
        binding.autoupdateswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchonoff = true;


                } else {
                    switchonoff = false;


                }
            }
        });

    }
}
