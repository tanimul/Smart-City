package com.example.nirmol_nogori;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.Ui.Door_to_Door_Service;
import com.example.nirmol_nogori.Ui.ProfileSetting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Programviewholder> {

    private ArrayList<String> location;
    private OnItemClickListener listener;


    public LocationAdapter(ArrayList<String> location, OnItemClickListener listener) {
        this.location = location;
        this.listener = listener;
    }


    public Programviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.location_card, viewGroup, false);
        return new Programviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Programviewholder programviewholder, int i) {
        final String locationName = location.get(i);
        programviewholder.locationName.setText(locationName);
        programviewholder.list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(locationName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return location.size();
    }

    public  class Programviewholder extends RecyclerView.ViewHolder{
        TextView locationName;
        CardView list_container;
        public Programviewholder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.card_location_name);
            list_container=itemView.findViewById(R.id.location_name_container);
        }

    }

    public interface OnItemClickListener {
        void OnItemClick(String location_name);
    }





}
