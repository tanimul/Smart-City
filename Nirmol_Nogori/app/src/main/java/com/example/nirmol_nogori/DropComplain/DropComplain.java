package com.example.nirmol_nogori.DropComplain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.nirmol_nogori.Fragment.HomeDropComplainFragment;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityDropComplainBinding;

public class DropComplain extends AppCompatActivity {
    private ActivityDropComplainBinding binding;
    private static final String TAG = "Drop_Complain_Activity";
    private Fragment selectedfragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDropComplainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (selectedfragment == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.dropcomplain_fragment_container, new HomeDropComplainFragment()).commit();
        }
    }
}
