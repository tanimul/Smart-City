package com.example.nirmol_nogori.FragmentAdmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public Profile_Admin_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //for testing
        final String userid = "WM1vIUC6esTbafyXAE69UvwTDLUED";

        databaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(userid);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load("" + dataSnapshot.child("imageurl").getValue()).into(binding.adminPhoto);
                binding.textviewAdminName.setText("" + dataSnapshot.child("name").getValue());
                binding.textviewAdminemail.setText("" + dataSnapshot.child("email").getValue());
                binding.textviewAdminphone.setText("" + dataSnapshot.child("mobile").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }
}
