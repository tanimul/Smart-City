package com.example.nirmol_nogori.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Paint;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.User.Login_User;
import com.example.nirmol_nogori.User.Password_Reset_Activity;
import com.example.nirmol_nogori.databinding.ActivityHomeMenuBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class Home_Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private ActivityHomeMenuBinding binding;
    private static final String TAG = "Home_Menu";
    private FirebaseAuth firebaseAuth;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                binding.homeDrwerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack, null));
        binding.homeDrwerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null) {
            binding.homeNavigation.setCheckedItem(R.id.nav_home);
        }

        binding.homeNavigation.setNavigationItemSelectedListener(this);

        binding.homeActivty.buttonFindNearestDustbine.setOnClickListener(this);
        binding.homeActivty.buttonDropComplain.setOnClickListener(this);
        binding.homeActivty.buttonDoorToDoorCleaningService.setOnClickListener(this);
        binding.homeActivty.buttonNewsTrend.setOnClickListener(this);
        binding.homeActivty.textViewVolunteerForm.setOnClickListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "navigation item selected");
        switch (item.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "navigation home item slelected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_share:
                share();
                break;

            case R.id.nav_rate_me:
                rating();
                break;

            case R.id.nav_volunteer:
                Toast.makeText(this, "navigation volunteer item slelected", Toast.LENGTH_SHORT).show();
//Todo volunteer activity create : Rehan
                break;
            case R.id.nav_term_condition:
                Toast.makeText(this, "navigation term condition item slelected", Toast.LENGTH_SHORT).show();


                break;

            case R.id.nav_thanks_credit:
                Toast.makeText(this, "navigation thanks credit item slelected", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_about:
                Toast.makeText(this, "navigation about item slelected", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_setting:
                Toast.makeText(this, "navigation setting item slelected", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_logout:
                logout();
                break;


        }
        binding.homeDrwerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == binding.homeActivty.buttonDoorToDoorCleaningService) {
            Toast.makeText(this, "DoorToDoorCleaningService item slelected", Toast.LENGTH_SHORT).show();

        } else if (v == binding.homeActivty.buttonDropComplain) {
            Toast.makeText(this, "DropComplain item slelected", Toast.LENGTH_SHORT).show();

        } else if (v == binding.homeActivty.buttonFindNearestDustbine) {
            startActivity(new Intent(Home_Menu.this, Find_Nearest_Dustbine.class));

        } else if (v == binding.homeActivty.buttonNewsTrend) {
            Toast.makeText(this, "NewsTrend item slelected", Toast.LENGTH_SHORT).show();

        } else if (v == binding.homeActivty.textViewVolunteerForm) {
            Toast.makeText(this, "VolunteerForm item slelected", Toast.LENGTH_SHORT).show();

        }

    }

    //Todo some work incomplete in rating
    //rate the apps
    private void rating() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    //Todo some work incomplete in share
    //share the apps
    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        ApplicationInfo api = getApplicationContext().getApplicationInfo();
        String apkpath = api.sourceDir;
        /// intent.putExtra(Intent.EXTRA_TEXT, "apps address "))
        intent.putExtra(Intent.EXTRA_TEXT, Uri.fromFile(new File(apkpath)));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share with"));
    }

    //logout current user
    private void logout() {
        firebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(Home_Menu.this, Login_User.class));
    }


}
