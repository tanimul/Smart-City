package com.example.nirmol_nogori.DropComplain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.nirmol_nogori.Adapter.ComplainAdapter;
import com.example.nirmol_nogori.Adapter.RepostAdapter;
import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.Model.Repost;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityComplainDetailsActivtyBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComplainDetailsActivity extends AppCompatActivity {
    private ActivityComplainDetailsActivtyBinding binding;
    String postid;
    private RecyclerView recyclerView;
    private ComplainAdapter complainAdapter;
    private List<Complain> complains;
    private static final String TAG = "Complains_Dtls_Fragment";

    String  publisherid;
    FirebaseUser firebaseUser;
    private RepostAdapter repostAdapter;
    private List<Repost> repostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityComplainDetailsActivtyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
//
//        postid = sharedPreferences.getString("postid", "none");

        Bundle intent=getIntent().getExtras();
        if(intent !=null){
            postid = intent.getString("complainid");
            Log.d("ddddd","post id:"+postid);

        }


        recyclerView = findViewById(R.id.complain_details_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        complains = new ArrayList<>();
        complainAdapter = new ComplainAdapter(this, complains);
        recyclerView.setAdapter(complainAdapter);

        readComplains(postid);



        binding.backfromPostdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ComplainDetailsActivity.this, DropComplain.class));
                finish();
            }
        });


        binding.complainRepostRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        binding.complainRepostRecyclerView.setLayoutManager(linearLayoutManager1);
        repostList = new ArrayList<>();
        repostAdapter = new RepostAdapter(this,repostList);
        binding.complainRepostRecyclerView.setAdapter(repostAdapter);


        readrepost(postid);
    }

    private void readrepost(String postid) {
        String post_id=postid;
        Log.d("dddd","ff:"+postid);
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
        final String post_id=postid;
        Log.d("dddd","read:"+postid);
        Log.d("dddd","read::"+post_id);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complains");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complains.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Complain complain = snapshot1.getValue(Complain.class);
                            if (post_id.equals(complain.getComplainid())) {
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
