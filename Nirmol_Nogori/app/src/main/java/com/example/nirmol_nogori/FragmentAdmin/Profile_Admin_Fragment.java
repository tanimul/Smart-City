package com.example.nirmol_nogori.FragmentAdmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.FragmentProfileAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Admin_Fragment extends Fragment {
    private FragmentProfileAdminBinding binding;
    private DatabaseReference databaseReference;
    private static final String TAG = "Profile_Admin_Fragment";

    public Profile_Admin_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //for testing

        final Bundle admininformation = getActivity().getIntent().getExtras();
        if (admininformation != null) {

            String adminid = admininformation.getString("adminid");
            String name = admininformation.getString("name");
            String email = admininformation.getString("email");
            String phoneno = admininformation.getString("phoneno");
            String imageurl = admininformation.getString("imageurl");

            Log.d(TAG, "" + adminid);
            Log.d(TAG, "" + name);
            Log.d(TAG, "" + email);
            Log.d(TAG, "" + phoneno);
            Log.d(TAG, "" + imageurl);

            informationset(adminid, name, email, phoneno, imageurl);
        }

        return view;
    }

    private void informationset(String adminid, String name, String email, String phonono, String imageurl) {

        binding.textviewAdminName.setText("" + name);
        binding.textviewAdminemail.setText("" + email);
        binding.textviewAdminphone.setText("" + phonono);
        Picasso.get().load(imageurl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(binding.adminPhoto);

    }
}
