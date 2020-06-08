package com.example.nirmol_nogori;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.Model.Cleaner;

import java.util.ArrayList;
import java.util.List;

public class CleanerAdapter extends RecyclerView.Adapter<CleanerAdapter.Cleanerholder> {

    private List<Cleaner> cleaners = new ArrayList<>();

    public CleanerAdapter(List<Cleaner> cleaners) {
        this.cleaners = cleaners;
    }

    @NonNull
    @Override
    public Cleanerholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clener_card, parent, false);
        return new Cleanerholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Cleanerholder holder, int position) {

        final Cleaner cleaner = cleaners.get(position);

        holder.name.setText(cleaner.getName());
        holder.location.setText(cleaner.getLocation());
        holder.t_fair.setText(cleaner.getTotal_fair());
        holder.ratingBar.setRating(cleaner.getRating());
       // holder.imageView.setImageResource(cleaner.getImageurl());

    }

    @Override
    public int getItemCount() {
        return cleaners.size();
    }

    public class Cleanerholder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView location;
        private TextView t_fair;
        private RatingBar ratingBar;
        private ImageView imageView;

        public Cleanerholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.card_cleanerTXT_name);
            location = itemView.findViewById(R.id.card_cleanerTXT_area);
            t_fair = itemView.findViewById(R.id.card_cleanerTXT_tFairValue);
            ratingBar = itemView.findViewById(R.id.card_ratingBar);
            imageView = itemView.findViewById(R.id.card_clenar_photo);
        }

    }

}
