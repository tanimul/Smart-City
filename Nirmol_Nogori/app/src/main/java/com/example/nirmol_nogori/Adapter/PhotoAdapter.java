package com.example.nirmol_nogori.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.Viewholder> {
    private Context context;
    private List<Complain> complains;

    public PhotoAdapter(Context mcontext, List<Complain> complains) {
        this.context = mcontext;
        this.complains = complains;
    }


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
