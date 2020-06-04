package com.example.nirmol_nogori;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Programviewholder> {

    //private String[] location;
    private ArrayList<String>location;

    public LocationAdapter(ArrayList<String> location) {
        this.location = location;
    }

//    public LocationAdapter(String[] location) {
//        this.location = location;
//    }

    public Programviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.location_card, viewGroup, false);
        return new Programviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Programviewholder programviewholder, int i) {
        String locationName = location.get(i);
        programviewholder.locationName.setText(locationName);
    }

    @Override
    public int getItemCount() {
        Log.d("dddddd",""+location.size());
        return location.size();
    }

    public class Programviewholder extends RecyclerView.ViewHolder {
        TextView locationName;

        public Programviewholder(@NonNull View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.card_location_name);

        }
    }


}
