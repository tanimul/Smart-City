package com.example.nirmol_nogori.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.Fragment.ComplainDetailsFragment;
import com.example.nirmol_nogori.Model.Repost;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.Ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.util.List;

public class RepostAdapter extends RecyclerView.Adapter<RepostAdapter.Viewholder> {

    private Context mContext;
    private List<Repost> mrepost;
    private FirebaseUser firebaseUser;

    public RepostAdapter(Context mContext, List<Repost> mrepost) {
        this.mContext = mContext;
        this.mrepost = mrepost;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.repostcard, parent, false);
        return new RepostAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Repost repost = mrepost.get(position);
        holder.repost.setText(repost.getRepostdetails());
        Picasso.get().load(repost.getRepostimage())
                .placeholder(R.drawable.ic_image)
                .fit()
                .centerCrop()
                .into(holder.repostimage);
        getUserInfo(holder.imageprofile,holder.user_name,repost.getUserid());

//        holder.comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(mContext, MainActivity.class);
//                intent.putExtra("publisherid",comment.getPublisher());
//                mContext.startActivity(intent);
//            }
//        });
//        holder.imageprofile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(mContext, MainActivity.class);
//                intent.putExtra("publisherid",comment.getPublisher());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mrepost.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public ImageView imageprofile,repostimage;
        public TextView user_name, repost;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageprofile = itemView.findViewById(R.id.image_user_profile);
            user_name = itemView.findViewById(R.id.user_name);
            repost = itemView.findViewById(R.id.repostdetails);
            repostimage = itemView.findViewById(R.id.repostimage);
        }


    }

    private void getUserInfo(final ImageView imageView, final TextView username, String userid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Picasso.get().load(user.getUser_image_url())
                        .placeholder(R.drawable.ic_user)
                        .fit()
                        .centerCrop()
                        .into(imageView);
                username.setText(user.getFirst_name()+" "+user.getLast_name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
