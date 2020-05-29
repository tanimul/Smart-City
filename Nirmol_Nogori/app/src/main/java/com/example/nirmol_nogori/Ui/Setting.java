package com.example.nirmol_nogori.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

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

        //check & show dark theme on or off
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.darkmodeswitch.setChecked(true);
        } else {
            binding.darkmodeswitch.setChecked(false);
        }

        //check & show autoupdate on or off
        if (switchonoff == true) {
            binding.autoupdateswitch.setChecked(true);

        } else {
            binding.autoupdateswitch.setChecked(false);
        }


        //profile setting
        binding.enterprofilefromsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting.this, ProfileSetting.class));
            }
        });


        //Todo create Dark mode
        //dark mode setting
        binding.darkmodeswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

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
