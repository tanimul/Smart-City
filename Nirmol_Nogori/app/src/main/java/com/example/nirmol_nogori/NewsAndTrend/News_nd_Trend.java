package com.example.nirmol_nogori.NewsAndTrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.nirmol_nogori.DoorToDoor.GetReviewFromuser;
import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.Adapter.NewsAdapter;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityNewsNdTrendBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class News_nd_Trend extends AppCompatActivity {
    private ActivityNewsNdTrendBinding binding;
    private static final String TAG = "News_and_Trend";
    private ArrayList<News> newslist = new ArrayList<News>();
    private RecyclerView rc_newstrend;
    private DatabaseReference databaseReference;
    NewsAdapter newsAdapter;
    int limitationshow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsNdTrendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rc_newstrend = findViewById(R.id.news_recyclerview);
        rc_newstrend.setFitsSystemWindows(true);
        rc_newstrend.setLayoutManager(new LinearLayoutManager(this));
        rc_newstrend.setHasFixedSize(true);
        newsAdapter = new NewsAdapter(News_nd_Trend.this, newslist);
        rc_newstrend.setAdapter(newsAdapter);

        readnews();

    }

    private void readnews() {
        final ProgressDialog Dialog = new ProgressDialog(News_nd_Trend.this);
        Dialog.setMessage("Please wait ...");
        Dialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("News_Trend");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newslist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                        Dialog.dismiss();
                        limitationshow++;
                        String key = snapshot.getKey();
                        News news1 = snapshot.getValue(News.class);
                        if (limitationshow <= 20) {
                            newslist.add(news1);
                        }
                        Log.d(TAG, "Key:" + key + "news name:" + news1.getNewstitle());
                    }
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "something is wrong:");
            }
        });
    }
}
