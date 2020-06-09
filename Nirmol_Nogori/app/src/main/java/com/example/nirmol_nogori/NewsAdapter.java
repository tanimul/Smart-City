package com.example.nirmol_nogori;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirmol_nogori.Model.Cleaner;
import com.example.nirmol_nogori.Model.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.Newsholder> {
    private ArrayList<News> news = new ArrayList<>();
    Context context;

    public NewsAdapter(Context context, ArrayList<News> news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public NewsAdapter.Newsholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newscard, parent, false);
        return new NewsAdapter.Newsholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.Newsholder holder, int position) {

        final News newsTrend = news.get(position);

        holder.news_name.setText(newsTrend.getNews_name());
        holder.news_src.setText(newsTrend.getSrc());
        Picasso.get().load(newsTrend.getNews_img_url())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class Newsholder extends RecyclerView.ViewHolder {
        private TextView news_name;
        private TextView news_src;
        private ImageView imageView;

        public Newsholder(@NonNull View itemView) {
            super(itemView);

            news_name = itemView.findViewById(R.id.card_newsTitle);
            news_src = itemView.findViewById(R.id.card_newssrc);
            imageView = itemView.findViewById(R.id.card_news_photo);
        }

    }
}
