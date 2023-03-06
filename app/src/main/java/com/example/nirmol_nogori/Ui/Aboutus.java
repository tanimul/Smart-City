package com.example.nirmol_nogori.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityAboutusBinding;

public class Aboutus extends AppCompatActivity implements View.OnClickListener {
    private ActivityAboutusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backfromabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Aboutus.this, Home_Menu.class));
                finish();
            }
        });

        binding.facebook.setOnClickListener(this);
        binding.email.setOnClickListener(this);
        binding.instragram.setOnClickListener(this);
        binding.github.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.facebook) {
            try {
                getPackageManager()
                        .getPackageInfo("com.example.nirmol_nogori", 0); //Checks if FB is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/fuhad.hasan.315")));  //Trys to make intent with FB's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/fuhad.hasan.315"))); //catches and opens a url to the desired page
            }
        } else if (v.getId() == R.id.email) {
            try {
                getPackageManager()
                        .getPackageInfo("com.example.nirmol_nogori", 0); //Checks if email is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://mail.google.com/mail/u/0/#inbox"))); //Trys to make intent with Instagram's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://mail.google.com/mail/u/0/#inbox"))); //catches and opens a url to the desired page
            }

        } else if (v.getId() == R.id.instragram) {

            try {
                getPackageManager()
                        .getPackageInfo("com.example.nirmol_nogori", 0); //Checks if Instagram is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/fuad7594/"))); //Trys to make intent with Instagram's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/fuad7594/"))); //catches and opens a url to the desired page
            }
        } else {
            try {
                getPackageManager()
                        .getPackageInfo("com.example.nirmol_nogori", 0); //Checks if github is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Tanimul"))); //Trys to make intent with Instagram's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Tanimul"))); //catches and opens a url to the desired page
            }
        }

    }
}
