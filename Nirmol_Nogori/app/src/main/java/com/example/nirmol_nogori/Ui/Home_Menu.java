package com.example.nirmol_nogori.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.User.Login_User;
import com.example.nirmol_nogori.databinding.ActivityHomeMenuBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Home_Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "navigation item selected");
        switch (item.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "navigation home item slelected", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_share:
                Toast.makeText(this, "navigation share item slelected", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_rate_me:
                Toast.makeText(this, "navigation rate me item slelected", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_volunteer:
                Toast.makeText(this, "navigation volunteer item slelected", Toast.LENGTH_SHORT).show();

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

    //logout current user
    private void logout() {
        firebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(Home_Menu.this, Login_User.class));
    }
}
