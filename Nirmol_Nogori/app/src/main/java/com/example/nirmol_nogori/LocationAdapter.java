package com.example.nirmol_nogori;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Programviewholder> {

    private ArrayList<String> location;
    private OnItemClickListener listener;




    public LocationAdapter(ArrayList<String> location) {
        this.location = location;
    }

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
        return location.size();
    }

    public  class Programviewholder extends RecyclerView.ViewHolder{
        TextView locationName;

        public Programviewholder(@NonNull View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.card_location_name);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(v,position);
                    }

                }
            });


        }

    }

    public interface OnItemClickListener {
        void OnItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = listener;
    }


}
