package com.example.nirmol_nogori.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.DropComplain.ComplainDetailsActivity;
import com.example.nirmol_nogori.DropComplain.PostDropComplain;
import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.DropComplain.UserProfileActivty;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ViewHolder> {

    public Context mcontext;
    public List<Complain> complains;
    private FirebaseUser firebaseUser;


    public ComplainAdapter(Context mcontext, List<Complain> complains) {
        this.mcontext = mcontext;
        this.complains = complains;
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
                Log.d("ddddd", "" + complain.getUserid());

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
                Log.d("ddddd", "Complain id:" + complain.getComplainid());
                mcontext.startActivity(intent);
            }
        });


        holder.repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, PostDropComplain.class);
                intent.putExtra("complain_id", "" + complain.getComplainid());
                ((Activity) mcontext).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return complains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageprofile, complainImage, save;
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
