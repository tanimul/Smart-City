package com.example.nirmol_nogori.DropComplain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.nirmol_nogori.Adapter.RepostAdapter;
import com.example.nirmol_nogori.DoorToDoor.GetReviewFromuser;
import com.example.nirmol_nogori.Interface.RepostClickInterface;
import com.example.nirmol_nogori.Model.Cleaner;
import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.Model.Repost;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.Ui.Home_Menu;
import com.example.nirmol_nogori.databinding.ActivityComplainDetailsActivtyBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComplainDetailsActivity extends AppCompatActivity implements RepostClickInterface {
    private ActivityComplainDetailsActivtyBinding binding;
    String re_postid, complainsid;
    private static final String TAG = "Complains_Dtls_Activity";
    String publisherid, adminid = "";
    FirebaseUser firebaseUser;
    private RepostAdapter repostAdapter;
    private List<Repost> repostList;
    private static boolean deleterequest = false;
    private static boolean editrequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplainDetailsActivtyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
//        postid = sharedPreferences.getString("postid", "none");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            complainsid = intent.getString("complainid");
            adminid = intent.getString("adminid");
            publisherid = intent.getString("complainerid");
            Log.d(TAG, "complain id:" + complainsid);
            Log.d(TAG, "admin id:" + adminid);
            Log.d(TAG, "publisher id:" + publisherid);

        }




        binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ComplainDetailsActivity.this, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Toast.makeText(ComplainDetailsActivity.this, "Request for complain update", Toast.LENGTH_SHORT).show();
                                editrequest = true;
                                readComplains(complainsid);
                                return true;
                            case R.id.delete:
                                deleterequest = true;
                                readComplains(complainsid);
                                return true;

                            case R.id.report:
                              //  Toast.makeText(ComplainDetailsActivity.this, "Succesfully report added.", Toast.LENGTH_SHORT).show();
                                report(publisherid, complainsid, firebaseUser.getUid());
                                return true;

                            default:
                                return false;

                        }
                    }
                });

                popupMenu.inflate(R.menu.complainmenu);

                //admin id check if true then it's an admin id
                if (adminid.equals("adminsecretid")) {
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.report).setVisible(false);
                } else if (!publisherid.equals(firebaseUser.getUid())) {
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                } else {
                    popupMenu.getMenu().findItem(R.id.report).setVisible(false);
                }
                popupMenu.show();
            }
        });

        if (firebaseUser != null) {
            issaved(complainsid, binding.savecomplain);
        }

        readComplains(complainsid);

        binding.backfromPostdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ComplainDetailsActivity.this, DropComplain.class));
                finish();
            }
        });


        binding.addrepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComplainDetailsActivity.this, PostDropComplain.class);
                intent.putExtra("complain_id", "" + complainsid);
                intent.putExtra("edit_request", false);
                startActivity(intent);
            }
        });
        

        binding.savecomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.savecomplain.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference("Saves").child(firebaseUser.getUid())
                            .child(complainsid).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference("Saves").child(firebaseUser.getUid())
                            .child(complainsid).removeValue();
                }
            }
        });

        binding.repostdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleterepost(complainsid, re_postid);

            }
        });
        binding.repostedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editrequest = true;
                readComplains(complainsid);
                binding.repostedit.setVisibility(View.GONE);
                binding.repostdelete.setVisibility(View.GONE);
            }
        });
        binding.repostreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                report(publisherid, complainsid, firebaseUser.getUid());
                binding.repostreport.setVisibility(View.GONE);
            }
        });


        binding.complainRepostRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        binding.complainRepostRecyclerView.setLayoutManager(linearLayoutManager1);
        repostList = new ArrayList<>();
        repostAdapter = new RepostAdapter(this, repostList, this);
        binding.complainRepostRecyclerView.setAdapter(repostAdapter);


        readrepost(complainsid);
    }


    private void deleterepost(final String complainsid, final String postid) {
        FirebaseDatabase.getInstance().getReference("Repost")
                .child(complainsid).child(postid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ComplainDetailsActivity.this, "Succesfully  deleted.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "delete request complainid:" + complainsid);
                            binding.repostedit.setVisibility(View.GONE);
                            binding.repostdelete.setVisibility(View.GONE);
                            binding.repostreport.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(ComplainDetailsActivity.this, "Somethings is wrong.", Toast.LENGTH_SHORT).show();
                            binding.repostedit.setVisibility(View.GONE);
                            binding.repostdelete.setVisibility(View.GONE);
                            binding.repostreport.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void report(final String publisherid, final String postid, final String uid) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reports")
                .child(publisherid).child(postid);
        Log.d(TAG, "" + postid);
        Log.d(TAG, "" + uid);
        Log.d(TAG, "" + publisherid);
//        Log.d(TAG, "key" + dataSnapshot1.getKey());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d(TAG, "" + postid);
                    Log.d(TAG, "" + uid);
                    Log.d(TAG, "" + publisherid);
                    Log.d(TAG, "key" + dataSnapshot1.getKey());

                    if (dataSnapshot1.getKey() == uid) {
                     //   Toast.makeText(ComplainDetailsActivity.this, "already you given a report for this post", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(ComplainDetailsActivity.this, "Succesfully report added.", Toast.LENGTH_SHORT).show();
//                        databaseReference.child(uid).setValue(true);
//                        updateuserinformation(publisherid);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateuserinformation(String publisherid) {
        Log.d(TAG, publisherid);

        final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Users")
                .child(publisherid);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                HashMap<String, Object> hashMap = new HashMap<>();
                int totalpinch = (users.getPinch() + 1);

                hashMap.put("pinch", totalpinch);

                Log.d(TAG, "after calculation:" + totalpinch);
                databaseReference3.updateChildren(hashMap);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void deletecomplain(String postid, String date, String username, final String complainid) {
        FirebaseDatabase.getInstance().getReference("Complains")
                .child(date).child(username).child(complainid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ComplainDetailsActivity.this, "Succesfully deleted.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "delete request complainid:" + complainid);
                            finish();

                        } else {
                            Toast.makeText(ComplainDetailsActivity.this, "Somethings is wrong.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void readrepost(String complainsid) {
        Log.d(TAG, "complainid:" + complainsid);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Repost").child(complainsid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                repostList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    if (dataSnapshot.getChildrenCount() != 0) {
                        //    binding.textrepostall.setVisibility(View.VISIBLE);
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

    private void readComplains(final String complainsid) {
        final String complains_id = complainsid;
        Log.d(TAG, "read:" + complains_id);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complains");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Complain complain = snapshot1.getValue(Complain.class);
                            if (complains_id.equals(complain.getComplainid())) {
                                binding.complainearea.setText("-at " + complain.getArea().toString());
                                binding.complainDescription.setText(complain.getDetails().toString());
                                Picasso.get().load(complain.getComplainimage()).into(binding.complainImage);
                                complainerinformation(complain.getUserid());
                                if (deleterequest == true) {
                                    Log.d(TAG, "deleterequest:" + deleterequest);
                                    deleterequest = false;
                                    deletecomplain(re_postid, complain.getDate(), complain.getUsername(), complain.getComplainid());

                                }
                                if (editrequest == true) {
                                    Log.d(TAG, "editrequest:" + editrequest);
                                    editrequest = false;
                                    editcomplain(complain.getDate(), complain.getUsername(), complain.getComplainid()
                                            , complain.getUserid(), complain.getArea(), complain.getComplainimage(), complain.getDetails());
                                }
                            }
                        }


                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void complainerinformation(String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        Log.d(TAG, "usrid:" + userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Picasso.get().load(user.getUser_image_url())
                        .placeholder(R.drawable.ic_user)
                        .fit()
                        .centerCrop()
                        .into(binding.complainerimageProfile);
                binding.complainername.setText(user.getFirst_name() + " " + user.getLast_name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void editcomplain(String date, String username, String complainid, String userid, String area,
                              String complainimage, String details) {
        Log.d(TAG, "edit request username:" + username);
        Log.d(TAG, "edit request complainid:" + complainid);

        Intent intent = new Intent(ComplainDetailsActivity.this, PostDropComplain.class);
        intent.putExtra("edit_request", true);
        intent.putExtra("edit_request_date", date);
        intent.putExtra("edit_request_usrname", username);
        intent.putExtra("edit_request_complainid", complainid);
        intent.putExtra("edit_request_userid", userid);
        intent.putExtra("edit_request_area", area);
        intent.putExtra("edit_request_complainimage", complainimage);
        intent.putExtra("edit_request_details", details);
        startActivity(intent);

    }

    private void issaved(final String complainid, final ImageView imageView) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Saves")
                .child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(complainid).exists()) {
                    imageView.setImageResource(R.drawable.ic_bookmark_black);
                    imageView.setTag("saved");

                } else {
                    imageView.setImageResource(R.drawable.ic_bookmark_border);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void OnRepostLongClick(String userid, String complainid, String repostid) {
        Log.d(TAG, "long click:" + repostid);
        re_postid = repostid;
        publisherid = userid;
        complainsid = complainid;
        //binding.repostlongclicklayout.setVisibility(View.VISIBLE);
        if (publisherid.equals(firebaseUser.getUid())) {
            binding.repostedit.setVisibility(View.VISIBLE);
            binding.repostdelete.setVisibility(View.VISIBLE);
            Log.d(TAG, "user:");
        } else {
            binding.repostreport.setVisibility(View.VISIBLE);
            Log.d(TAG, "visitor:");
        }
    }

    @Override
    public void OnRepostsingleClick(String repostid) {
        Log.d(TAG, "single click:" + repostid);
        binding.repostedit.setVisibility(View.GONE);
        binding.repostdelete.setVisibility(View.GONE);
        binding.repostreport.setVisibility(View.GONE);

    }
}
