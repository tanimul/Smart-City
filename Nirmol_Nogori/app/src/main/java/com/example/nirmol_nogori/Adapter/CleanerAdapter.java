package com.example.nirmol_nogori.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.Model.Cleaner;
import com.example.nirmol_nogori.R;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class CleanerAdapter extends RecyclerView.Adapter<CleanerAdapter.Cleanerholder> {
    private static final String TAG = "Cleaner_Adapter";
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
    public void onBindViewHolder(@NonNull final Cleanerholder holder, final int position) {

        final Cleaner cleaner = cleaners.get(position);
        holder.name.setText(cleaner.getName());
        holder.location.setText(cleaner.getLocation());
        holder.ratingBar.setRating(cleaner.getRating());
        Picasso.get().load(cleaner.getImageurl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        Log.d(TAG, "" + cleaners.size());

        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "calling" + cleaner.getPhoneno());
                Context context = v.getContext();
                requestcleaner(context, cleaner.getPhoneno());
            }
        });


    }

    private void requestcleaner(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "" + number));
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return cleaners.size();
    }

    public class Cleanerholder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView location;
        private RatingBar ratingBar;
        private ImageView imageView;
        private Button request;

        public Cleanerholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.card_cleanerTXT_name);
            location = itemView.findViewById(R.id.card_cleanerTXT_area);
            ratingBar = itemView.findViewById(R.id.card_ratingBar);
            imageView = itemView.findViewById(R.id.card_clenar_photo);
            request = itemView.findViewById(R.id.card_cleaner_callbutton);

        }

    }

}
