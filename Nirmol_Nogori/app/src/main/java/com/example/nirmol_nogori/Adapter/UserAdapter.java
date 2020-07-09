package com.example.nirmol_nogori.Adapter;

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

import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.DropComplain.UserProfileActivty;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Userviewholder> {
    private static final String TAG = "User_Adapter";
    private ArrayList<Users> userlist;
    private Context context;


    public UserAdapter(ArrayList<Users> userlist, Context context) {
        this.userlist = userlist;
        this.context = context;
    }

    @NonNull
    @Override
    public Userviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.usercard, parent, false);
        return new UserAdapter.Userviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Userviewholder holder, int position) {
        final Users users = userlist.get(position);
        holder.username.setText(users.getFirst_name() + " " + users.getLast_name());

        if (users.getPinch() > 10) {
            holder.username.setTextColor(context.getResources().getColor(R.color.colorred));
        } else {
            holder.username.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }

        Picasso.get().load(users.getUser_image_url())
                .placeholder(R.drawable.ic_user)
                .fit()
                .centerCrop()
                .into(holder.userImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                editor.putString("userid", users.getUserid());
//                editor.apply();

                Intent intent = new Intent(context, UserProfileActivty.class);
                intent.putExtra("userid", users.getUserid());
                Log.d("ddddd", "user id:" + users.getUserid());


//                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
//                        .add(R.id.dropcomplain_fragment_container, new UserProfileFragment()).commit();

                context.startActivity(intent);

            }
        });


        Log.d(TAG, "" + userlist.size());
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class Userviewholder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView username;

        public Userviewholder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.card_user_photo);
            username = itemView.findViewById(R.id.card_user_name);

        }
    }

}
