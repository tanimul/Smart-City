package com.example.nirmol_nogori;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Programviewholder> {

    private String[] data;

    public LocationAdapter(String[] data) {
        this.data = data;
    }

    public Programviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.location_card, viewGroup, false);
        return new Programviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Programviewholder programviewholder, int i) {
        String title = data[i];
        programviewholder.textView.setText(title);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class Programviewholder extends RecyclerView.ViewHolder {
        TextView textView;

        public Programviewholder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.card_location_name);

        }
    }


}
