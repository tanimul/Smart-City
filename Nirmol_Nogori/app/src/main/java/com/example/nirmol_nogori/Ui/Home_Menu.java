package com.example.nirmol_nogori.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirmol_nogori.Aboutus;
import com.example.nirmol_nogori.DoorToDoor.Door_to_Door_Location;
import com.example.nirmol_nogori.DropComplain.DropComplain;
import com.example.nirmol_nogori.FindNearestDustbine.Find_Nearest_Dustbine;
import com.example.nirmol_nogori.NewsAndTrend.News_nd_Trend;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.Term_Condition;
import com.example.nirmol_nogori.ThanksCredit;
import com.example.nirmol_nogori.databinding.ActivityHomeMenuBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class Home_Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private ActivityHomeMenuBinding binding;
    private static final String TAG = "Home_Menu";
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    Toolbar toolbar;

    private ImageView headerPic;
    private TextView headerName;
    private long backpressed;
    private Toast backtost;

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


//        View header = binding.homeNavigation.getHeaderView(0);
//        headerName = header.findViewById(R.id.header_name);
//        headerPic = header.findViewById(R.id.header_photo);
//
//        //fill up current user information in nav header
//        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
//        updateNavHeader(firebaseUser.getUid());


        binding.homeNavigation.setNavigationItemSelectedListener(this);

        binding.homeActivty.buttonFindNearestDustbine.setOnClickListener(this);
        binding.homeActivty.buttonDropComplain.setOnClickListener(this);
        binding.homeActivty.buttonDoorToDoorCleaningService.setOnClickListener(this);
        binding.homeActivty.buttonNewsTrend.setOnClickListener(this);
        //binding.homeActivty.textViewVolunteerForm.setOnClickListener(this);

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

//            case R.id.nav_volunteer:
//                Toast.makeText(this, "navigation volunteer item slelected", Toast.LENGTH_SHORT).show();
//
//                break;
            case R.id.nav_term_condition:
                startActivity(new Intent(Home_Menu.this, Term_Condition.class));
                break;

            case R.id.nav_thanks_credit:
                startActivity(new Intent(Home_Menu.this, ThanksCredit.class));
                break;

            case R.id.nav_about:
                startActivity(new Intent(Home_Menu.this, Aboutus.class));
                break;

            case R.id.nav_setting:
                startActivity(new Intent(Home_Menu.this, Setting.class));
                break;

            case R.id.nav_logout:
                logout();

                Intent intent = new Intent(Home_Menu.this, MainActivity.class);
                intent.putExtra("finish", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();

                break;


        }
        binding.homeDrwerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == binding.homeActivty.buttonDoorToDoorCleaningService) {
            startActivity(new Intent(Home_Menu.this, Door_to_Door_Location.class));

        } else if (v == binding.homeActivty.buttonDropComplain) {
            startActivity(new Intent(Home_Menu.this, DropComplain.class));

        } else if (v == binding.homeActivty.buttonFindNearestDustbine) {
            startActivity(new Intent(Home_Menu.this, Find_Nearest_Dustbine.class));

        } else if (v == binding.homeActivty.buttonNewsTrend) {
            startActivity(new Intent(Home_Menu.this, News_nd_Trend.class));

        }
//        else if (v == binding.homeActivty.textViewVolunteerForm) {
//            Toast.makeText(this, "VolunteerForm item slelected", Toast.LENGTH_SHORT).show();
//
//        }

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
    }

    //for Double press for Exit
    @Override
    public void onBackPressed() {

        if (backpressed + 2000 > System.currentTimeMillis()) {
            backtost.cancel();
            super.onBackPressed();
            return;
        } else {
            backtost = Toast.makeText(Home_Menu.this, "press BACK again to Exit", Toast.LENGTH_SHORT);
            backtost.show();
        }

        backpressed = System.currentTimeMillis();

    }


}
