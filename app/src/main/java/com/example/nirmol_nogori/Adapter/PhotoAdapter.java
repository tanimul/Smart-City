package com.example.nirmol_nogori.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.DropComplain.ComplainDetailsActivity;
import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.Viewholder> {
    private Context context;
    private List<Complain> complains;
    public FragmentManager f_manager;
    private FirebaseUser firebaseUser;

    public PhotoAdapter(Context mcontext, List<Complain> complains) {
        this.context = mcontext;
        this.complains = complains;
    }

//    public PhotoAdapter(Context context, FragmentManager f_manager) {
//        this.context = context;
//        this.f_manager = f_manager;
//    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complainphotocard, parent, false);
        return new PhotoAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        final Complain complain = complains.get(position);
        Picasso.get().load(complain.getComplainimage())
                .placeholder(R.drawable.ic_image)
                .fit()
                .centerCrop()
                .into(holder.complainmage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                editor.putString("postid", complain.getComplainid());
//                editor.apply();

                Intent intent = new Intent(context, ComplainDetailsActivity.class);
                intent.putExtra("complainid", complain.getComplainid());
                intent.putExtra("complainerid", "" + complain.getUserid());
                Log.d("ddddd", "Complain id:" + complain.getComplainid());
                Log.d("ddddd", "Complainer id:" + complain.getUserid());

                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    intent.putExtra("adminid", "adminsecretid");
                } else {
                    intent.putExtra("adminid", "null");
                }

                context.startActivity(intent);

//                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.dropcomplain_fragment_container,
//                        new ComplainDetailsFragment()).commit();

//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.dropcomplain_fragment_container,
//                        new ComplainDetailsFragment()).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return complains.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        public ImageView complainmage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            complainmage = itemView.findViewById(R.id.complainimage);
        }
    }

}
