package com.example.nirmol_nogori.Adapter;

import android.content.Context;
import android.util.Log;
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
import com.example.nirmol_nogori.Model.Review;
import com.example.nirmol_nogori.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewwHolder> {
    private static final String TAG = "Review_Adapter";
    private ArrayList<Review> reviews = new ArrayList<>();
    Context context;

    public ReviewAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent, false);
        return new ReviewAdapter.ReviewwHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewwHolder holder, int position) {
        final Review review = reviews.get(position);
        holder.reviewername.setText(review.getUsername());
        holder.reviewerdate.setText(review.getDate());
        holder.reviewerreview.setText(review.getReview());
        holder.ratingBar.setRating(review.getRating());
        Picasso.get().load(review.getImageurl())
                .placeholder(R.drawable.ic_user)
                .fit()
                .centerCrop()
                .into(holder.reviewerImage);
        Log.d(TAG, "" + reviews.size());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public class ReviewwHolder extends RecyclerView.ViewHolder {

         TextView reviewername, reviewerdate, reviewerreview;
         ImageView reviewerImage;
         RatingBar ratingBar;

        public ReviewwHolder(@NonNull View itemView) {
            super(itemView);

            reviewername = itemView.findViewById(R.id.card_cleanerTXT_reviewer_name);
            reviewerdate = itemView.findViewById(R.id.card_cleanerTXT_reviewer_date);
            ratingBar = itemView.findViewById(R.id.card_ratingBar_reviewer);
            reviewerreview = itemView.findViewById(R.id.card_cleanerTXT_reviewer_review);
            reviewerImage = itemView.findViewById(R.id.card_reviewer_photo);

        }
    }
}
