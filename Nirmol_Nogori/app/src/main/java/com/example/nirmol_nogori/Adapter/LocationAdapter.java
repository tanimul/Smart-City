package com.example.nirmol_nogori.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Locationviewholder> {
    private static final String TAG = "Location_Adapter";
    private ArrayList<String> location;
    private OnItemClickListener listener;


    public LocationAdapter(ArrayList<String> location, OnItemClickListener listener) {
        this.location = location;
        this.listener = listener;
    }


    public Locationviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.location_card, viewGroup, false);
        return new Locationviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Locationviewholder locationviewholder, int i) {
        final String locationName = location.get(i);
        locationviewholder.locationName.setText(locationName);

        //Handle the location item
        locationviewholder.list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(locationName);
            }
        });

        Log.d(TAG,""+location.size());
    }

    @Override
    public int getItemCount() {
        return location.size();
    }

    public class Locationviewholder extends RecyclerView.ViewHolder {
        TextView locationName;
        CardView list_container;

        public Locationviewholder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.card_location_name);
            list_container = itemView.findViewById(R.id.location_name_container);
        }

    }

    public interface OnItemClickListener {
        void OnItemClick(String location_name);
    }

}
