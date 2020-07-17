package com.example.nirmol_nogori.NewsAndTrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //For delete the news by admin using Right swipe
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("News_Trend")
                            .child(newslist.get(position).getDate());


                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                News news = snapshot.getValue(News.class);
                                if (news.getNewstitle().equals(newslist.get(position).getNewstitle())
                                        && news.getSrc().equals(newslist.get(position).getSrc())
                                        && news.getNews_link().equals(newslist.get(position).getNews_link())) {
                                    databaseReference.child(snapshot.getKey()).removeValue();
                                    Log.d(TAG, "Deleted .");
                                    Toast.makeText(News_nd_Trend.this, "Deleted the news.", Toast.LENGTH_SHORT).show();

                                }
                            }
                            newsAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float imageWidth = ((BitmapDrawable) getResources().getDrawable(R.drawable.trash)).getBitmap().getWidth();
                    Paint p = new Paint();
                    Bitmap icon;
                    p.setColor(ContextCompat.getColor(News_nd_Trend.this, R.color.colorred));
                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                    c.drawRect(background, p);
                    c.clipRect(background);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trash);
                    RectF icon_dest = new RectF((float) itemView.getLeft() + imageWidth, (float) itemView.getTop() + ((height / 2) - (imageWidth / 2)), (float) itemView.getLeft() + 2 * imageWidth, (float) itemView.getBottom() - ((height / 2) - (imageWidth / 2)));
                    c.drawBitmap(bitmap, null, icon_dest, p);
                    c.restore();

                }

            }).attachToRecyclerView(binding.newsRecyclerview);
        }
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
