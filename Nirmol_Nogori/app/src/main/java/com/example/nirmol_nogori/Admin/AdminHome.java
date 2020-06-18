package com.example.nirmol_nogori.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.nirmol_nogori.FragmentAdmin.Door_to_Door_Admin_Fragment;
import com.example.nirmol_nogori.FragmentAdmin.Drop_Complain_Admin_Fragment;
import com.example.nirmol_nogori.FragmentAdmin.News_and_Trend_Admin_Fragment;
import com.example.nirmol_nogori.FragmentAdmin.Profile_Admin_Fragment;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityAdminHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityAdminHomeBinding binding;
    private BottomNavigationView bottomNavigationView;
    private Fragment selectedfragment = null;
    private static final String TAG = "Admin_Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (selectedfragment == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new Drop_Complain_Admin_Fragment()).commit();

        }

        binding.adminBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_drop_complain:
                Log.d(TAG, "Selected Admin Fragment: Drop Complain");
                selectedfragment = new Drop_Complain_Admin_Fragment();
                break;

            case R.id.nav_news_and_trend:
                Log.d(TAG, "Selected Admin Fragment: News and Trend");
                selectedfragment = new News_and_Trend_Admin_Fragment();
                break;

            case R.id.nav_door_to_door:
                Log.d(TAG, "Selected Admin Fragment: Door to Door");
                selectedfragment = new Door_to_Door_Admin_Fragment();
                break;
            case R.id.nav_profile:
                Log.d(TAG, "Selected Admin Fragment: Profile");
                selectedfragment = new Profile_Admin_Fragment();
                break;
        }


        if (selectedfragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, selectedfragment).commit();

        }

        return true;
    }

}
