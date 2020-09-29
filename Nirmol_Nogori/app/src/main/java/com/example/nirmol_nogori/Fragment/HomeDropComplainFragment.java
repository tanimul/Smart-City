package com.example.nirmol_nogori.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nirmol_nogori.Adapter.ComplainAdapter;
import com.example.nirmol_nogori.DropComplain.DropComplain;
import com.example.nirmol_nogori.DropComplain.PostDropComplain;
import com.example.nirmol_nogori.Model.Admin;
import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.DropComplain.UserProfileActivty;
import com.example.nirmol_nogori.databinding.FragmentHomeDropComplainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeDropComplainFragment extends Fragment implements View.OnClickListener {
    private FragmentHomeDropComplainBinding binding;
    private static final String TAG = "Home_Drop_Comp_Fragment";
    private RecyclerView recyclerView;
    private ComplainAdapter complainAdapter;
    private List<Complain> complains;
    ProgressBar progressBar;
    private int image_rec_code = 1;
    private Uri filepath_uri;
    private String userid;
    private long lastclicktime = 0;
    private String adminid;
    private int profilerequestcode = 0;


    public HomeDropComplainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeDropComplainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        progressBar = view.findViewById(R.id.progress_circular);
        recyclerView = view.findViewById(R.id.recycler_view_complainall);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        complains = new ArrayList<>();
        complainAdapter = new ComplainAdapter(getContext(), complains, false);//pass user as false
        recyclerView.setAdapter(complainAdapter);


        readComplains();

        Bundle bundle = new Bundle();
        if (bundle != null && Profile_Admin_Fragment.admin_id != null) {
            Log.d(TAG, "Admin request");
            Log.d(TAG, "Admin id:" + Profile_Admin_Fragment.admin_id);
            adminid = Profile_Admin_Fragment.admin_id;
            profilerequestcode = 1;
            updateprofilepic(adminid, profilerequestcode);

        } else {
            Log.d(TAG, "User request");
            userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            profilerequestcode = 0;
            updateprofilepic(userid, profilerequestcode);
        }


        binding.goaddcomplianFragment.setOnClickListener(this);
        binding.userPhoto.setOnClickListener(this);


        return view;
    }


    //for user profile
    private void updateprofilepic(String userid, final int profilerequestcode) {
        final String usesid = userid;
        Log.d(TAG, "uses id:" + usesid);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if (profilerequestcode != 1) {
            databaseReference = databaseReference.child("Users").child(usesid);
        } else {
            databaseReference = databaseReference.child("Admin").child(usesid);
        }

        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (profilerequestcode != 1) {
                    Users users = dataSnapshot.getValue(Users.class);
                    Picasso.get().load(users.getUser_image_url())
                            .placeholder(R.drawable.ic_user)
                            .fit()
                            .centerCrop()
                            .into(binding.userPhoto);

                } else {
                    Admin users = dataSnapshot.getValue(Admin.class);
                    Picasso.get().load(users.getImageurl())
                            .placeholder(R.drawable.ic_user)
                            .fit()
                            .centerCrop()
                            .into(binding.userPhoto);
                    binding.goaddcomplianFragment.setText(""+users.getName());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readComplains() {
        Log.d(TAG, "read");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complains");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complains.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Complain complain = snapshot1.getValue(Complain.class);
                            complains.add(complain);
                        }


                    }
                }
                Collections.reverse(complains);
                complainAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastclicktime < 1000) {
            return;
        }
        lastclicktime = SystemClock.elapsedRealtime();

        if (v == binding.goaddcomplianFragment) {
            if (profilerequestcode != 1) {
                startActivity(new Intent(getActivity(), PostDropComplain.class));
            }

        } else if (v == binding.userPhoto) {
            if (profilerequestcode != 1) {
                Log.d(TAG, "" + userid);
                Intent intent = new Intent(getContext(), UserProfileActivty.class);
                intent.putExtra("userid", userid);
                getActivity().startActivity(intent);
            }

        }

    }
}
