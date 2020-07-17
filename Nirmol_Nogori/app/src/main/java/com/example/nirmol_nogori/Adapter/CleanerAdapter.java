package com.example.nirmol_nogori.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.DoorToDoor.CleanerProfile;
import com.example.nirmol_nogori.Model.Cleaner;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.Ui.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CleanerAdapter extends RecyclerView.Adapter<CleanerAdapter.Cleanerholder> {
    private static final String TAG = "Cleaner_Adapter";
    private List<Cleaner> cleaners = new ArrayList<>();
    private long lastclicktime = 0;

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
                if (SystemClock.elapsedRealtime() - lastclicktime < 1000) {
                    return;
                }
                lastclicktime = SystemClock.elapsedRealtime();

                MainActivity.CLEANER_REQUEST = true;
                CleanerProfile.cleanername = cleaner.getName();
                CleanerProfile.cleanerarea = cleaner.getLocation();
                Log.d(TAG, "calling" + cleaner.getPhoneno());
                Context context = v.getContext();
                requestcleaner(context, cleaner.getPhoneno());
                Log.d(TAG, "Cleaner name: +" + cleaner.getName() + " Cleaner number: " + cleaner.getPhoneno() + " CLEANER_REQUEST:" + MainActivity.CLEANER_REQUEST);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastclicktime < 1000) {
                    return;
                }
                lastclicktime = SystemClock.elapsedRealtime();

                Context context = v.getContext();
                Intent intent = new Intent(context, CleanerProfile.class);
                intent.putExtra("cleaner_name", cleaner.getName());
                intent.putExtra("cleaner_area", cleaner.getLocation());
                intent.putExtra("cleaner_imageurl", cleaner.getImageurl());
                intent.putExtra("cleaner_ratings", cleaner.getRating());
                intent.putExtra("cleaner_totalfair", cleaner.getTotal_fair());
                intent.putExtra("cleaner_totalhr", cleaner.getTotal_hr());
                intent.putExtra("cleaner_phone", cleaner.getPhoneno());
                context.startActivity(intent);
            }
        });

    }

    //Request Cleaner by Call
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
