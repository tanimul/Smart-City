package com.example.nirmol_nogori.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nirmol_nogori.Adapter.ComplainAdapter;
import com.example.nirmol_nogori.Adapter.RepostAdapter;
import com.example.nirmol_nogori.DropComplain.DropComplain;
import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.Model.Repost;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.FragmentComplainDetailsBinding;
import com.example.nirmol_nogori.databinding.FragmentDoorToDoorAdminBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComplainDetailsFragment extends Fragment {
    private FragmentComplainDetailsBinding binding;
    String postid;
    private RecyclerView recyclerView;
    private ComplainAdapter complainAdapter;
    private List<Complain> complains;
    private static final String TAG = "Complains_Dtls_Fragment";

    String  publisherid;
    FirebaseUser firebaseUser;
    private RepostAdapter repostAdapter;
    private List<Repost> repostList;

    public ComplainDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComplainDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        postid = sharedPreferences.getString("postid", "none");


        recyclerView = view.findViewById(R.id.complain_details_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        complains = new ArrayList<>();
        complainAdapter = new ComplainAdapter(getContext(), complains);
        recyclerView.setAdapter(complainAdapter);

        readComplains(postid);


        binding.backfromPostdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DropComplain.class));
                getActivity().finish();
            }
        });


        binding.complainRepostRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        binding.complainRepostRecyclerView.setLayoutManager(linearLayoutManager1);
        repostList = new ArrayList<>();
        repostAdapter = new RepostAdapter(getContext(),repostList);
        binding.complainRepostRecyclerView.setAdapter(repostAdapter);


        readrepost(postid);

        return view;
    }

    private void readrepost(String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Repost").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                repostList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                      if(dataSnapshot.getChildrenCount() !=0){
                          binding.textrepostall.setVisibility(View.VISIBLE);
                          Repost repost = dataSnapshot1.getValue(Repost.class);
                          repostList.add(repost);
                      }
                }
                repostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readComplains(final String postid) {
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
                            if (postid.equals(complain.getComplainid())) {
                                complains.add(complain);
                            }
                        }


                    }
                }
                complainAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
