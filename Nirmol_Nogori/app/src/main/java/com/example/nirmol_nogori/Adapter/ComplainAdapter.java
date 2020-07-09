package com.example.nirmol_nogori.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.DropComplain.ComplainDetailsActivity;
import com.example.nirmol_nogori.DropComplain.PostDropComplain;
import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.DropComplain.UserProfileActivty;
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

import java.util.HashMap;
import java.util.List;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ViewHolder> {

    public Context mcontext;
    public List<Complain> complains;
    private FirebaseUser firebaseUser;
    private boolean user;
    private static final String TAG = "ComplainAdapter";


    public ComplainAdapter(Context mcontext, List<Complain> complains, boolean user) {
        this.mcontext = mcontext;
        this.complains = complains;
        this.user = user;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.complaincard, parent, false);
        return new ComplainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Complain complain = complains.get(position);
        Picasso.get().load(complain.getComplainimage())
                .placeholder(R.drawable.ic_image)
                .fit()
                .centerCrop()
                .into(holder.complainImage);

        holder.complain_desc.setText(complain.getDetails());
        holder.location.setText("-at " + complain.getArea());

        ComplainerInfo(holder.imageprofile, holder.username, complain.getUserid());
        if (firebaseUser != null) {
            issaved(complain.getComplainid(), holder.save);
        }

        holder.imageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                editor.putString("userid", complain.getUserid());
//                editor.apply();
                Intent intent = new Intent(mcontext, UserProfileActivty.class);
                intent.putExtra("userid", complain.getUserid());
                Log.d(TAG, "" + complain.getUserid());

//                ((FragmentActivity) mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.dropcomplain_fragment_container,
//                        new UserProfileFragment()).commit();

                mcontext.startActivity(intent);
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, UserProfileActivty.class);
                intent.putExtra("userid", complain.getUserid());
                Log.d("ddddd", "" + complain.getUserid());

                mcontext.startActivity(intent);

            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference("Saves").child(firebaseUser.getUid())
                            .child(complain.getComplainid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference("Saves").child(firebaseUser.getUid())
                            .child(complain.getComplainid()).removeValue();
                }
            }
        });
        holder.complainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences.Editor editor = mcontext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                editor.putString("postid", complain.getComplainid());
//                editor.apply();

                Intent intent = new Intent(mcontext, ComplainDetailsActivity.class);
                intent.putExtra("complainid", "" + complain.getComplainid());
                intent.putExtra("complainerid", "" + complain.getUserid());
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    intent.putExtra("adminid", "null");
                } else {
                    intent.putExtra("adminid", "adminsecretid");
                }
                Log.d(TAG, "Complain id:" + complain.getComplainid());
                Log.d(TAG, "complainer id:" + complain.getUserid());
                mcontext.startActivity(intent);
            }
        });


        holder.repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, PostDropComplain.class);
                intent.putExtra("complain_id", "" + complain.getComplainid());
                intent.putExtra("edit_request",false);
                ((Activity) mcontext).startActivity(intent);

            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mcontext, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Toast.makeText(mcontext, "Request for complain update", Toast.LENGTH_SHORT).show();
                                editcomplain(complain.getDate(), complain.getUsername(), complain.getComplainid()
                                        , complain.getUserid(), complain.getArea(), complain.getComplainimage(), complain.getDetails());
                                return true;
                            case R.id.delete:
                                FirebaseDatabase.getInstance().getReference("Complains")
                                        .child(complain.getDate()).child(complain.getUsername()).child(complain.getComplainid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(mcontext, "Succesfully deleted.", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "delete request date:" + complain.getDate());
                                                    Log.d(TAG, "delete request username:" + complain.getUsername());
                                                    Log.d(TAG, "delete request complainid:" + complain.getComplainid());

                                                } else {
                                                    Toast.makeText(mcontext, "Somethings is wrong.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                return true;

                            case R.id.report:
                                Toast.makeText(mcontext, "Succesfully report added.", Toast.LENGTH_SHORT).show();
                                report(complain.getUserid(),complain.getComplainid(), firebaseUser.getUid());
                                return true;

                            default:
                                return false;

                        }
                    }
                });

                popupMenu.inflate(R.menu.complainmenu);

                //admin id check if true then it's an admin id
                if (user == true) {
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.report).setVisible(false);
                } else if (!complain.getUserid().equals(firebaseUser.getUid())) {
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                } else {
                    popupMenu.getMenu().findItem(R.id.report).setVisible(false);
                }
                popupMenu.show();
            }
        });
    }

    private void editcomplain(String date, String username, String complainid, String userid, String area,
                              String complainimage, String details) {
        Log.d(TAG, "edit request username:" + username);
        Log.d(TAG, "edit request complainid:" + complainid);

        Intent intent = new Intent(mcontext, PostDropComplain.class);
        intent.putExtra("edit_request", true);
        intent.putExtra("edit_request_date", date);
        intent.putExtra("edit_request_usrname", username);
        intent.putExtra("edit_request_complainid", complainid);
        intent.putExtra("edit_request_userid", userid);
        intent.putExtra("edit_request_area", area);
        intent.putExtra("edit_request_complainimage", complainimage);
        intent.putExtra("edit_request_details", details);
        mcontext.startActivity(intent);

    }


    private void report(String userid, final String complainid, final String reportid) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reports").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(complainid).child(reportid).setValue(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return complains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageprofile, complainImage, save, more;
        public TextView location, username, complain_desc;
        public TextView repost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageprofile = itemView.findViewById(R.id.complainerimage_profile);
            complainImage = itemView.findViewById(R.id.complain_image);
            save = itemView.findViewById(R.id.savecomplain);
            location = itemView.findViewById(R.id.complainearea);
            username = itemView.findViewById(R.id.complainername);
            complain_desc = itemView.findViewById(R.id.complain_description);
            repost = itemView.findViewById(R.id.addrepost);
            more = itemView.findViewById(R.id.more);


        }


    }


    private void ComplainerInfo(final ImageView imageprofile, final TextView username, String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Picasso.get().load(user.getUser_image_url())
                        .placeholder(R.drawable.ic_user)
                        .fit()
                        .centerCrop()
                        .into(imageprofile);
                username.setText(user.getFirst_name() + " " + user.getLast_name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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


}
