package com.example.nirmol_nogori.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.NewsAdapter;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityNewsNdTrendBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Todo add query date wise show

public class News_nd_Trend extends AppCompatActivity {
    private ActivityNewsNdTrendBinding binding;
    private static final String TAG = "News_and_Trend";
    private ArrayList<News> newslist = new ArrayList<News>();
    private RecyclerView rc_newstrend;
    private DatabaseReference databaseReference;
    NewsAdapter newsAdapter;

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

        databaseReference = FirebaseDatabase.getInstance().getReference("News_Trend");
        databaseReference.keepSynced(true);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String key = dataSnapshot1.getKey();
                    Log.d(TAG, "Key:" + key);
                    News news1 = dataSnapshot1.getValue(News.class);
                    newslist.add(news1);

                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "something is wrong:" );
            }
        });
    }
}
