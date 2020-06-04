
package com.example.nirmol_nogori.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nirmol_nogori.LocationAdapter;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityDoorToDoorServiceBinding;

import java.util.List;

public class Door_to_Door_Service extends AppCompatActivity {
    private ActivityDoorToDoorServiceBinding binding;
    private static final String TAG = "Door_to_Door_Service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoorToDoorServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String[] language = {"Dhanmondi", "Kalabagan", "Mohammadpur", "Uttara", "Purbachal", "Gulshan", "Banani", "Mirpur", "Ashulia", "Motijhil", "Narayangonj", "Nilkhet"};
        RecyclerView programminglist = findViewById(R.id.location_recyclerview);
        programminglist.setLayoutManager(new LinearLayoutManager(this));
        programminglist.setAdapter(new LocationAdapter(language));

    }



}
