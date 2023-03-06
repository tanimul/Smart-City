package com.example.nirmol_nogori.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nirmol_nogori.Adapter.LocationAdapter;
import com.example.nirmol_nogori.Adapter.UserAdapter;
import com.example.nirmol_nogori.DoorToDoor.Door_to_Door_Location;
import com.example.nirmol_nogori.DoorToDoor.GetReviewFromuser;
import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.NewsAndTrend.News_nd_Trend;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.Ui.MainActivity;
import com.example.nirmol_nogori.databinding.ActivityDoorToDoorLocationBinding;
import com.example.nirmol_nogori.databinding.FragmentDropComplainAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class Drop_Complain_Admin_Fragment extends Fragment {
    private FragmentDropComplainAdminBinding binding;
    private static final String TAG = "Drop_Com_Admin_Fragment";
    private RecyclerView userlistrecyclerview;
    private DatabaseReference databaseReference;
    ArrayList<Users> userlist = new ArrayList<Users>();
    private android.app.AlertDialog.Builder alretdialog;
    private UserAdapter userAdapter;


    public Drop_Complain_Admin_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDropComplainAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        userlistrecyclerview = view.findViewById(R.id.user_recyclerview);
        userlistrecyclerview.setFitsSystemWindows(true);
        userlistrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new UserAdapter(userlist, getContext());
        userlistrecyclerview.setAdapter(userAdapter);

        readUser();

        binding.usersearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchuser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        delete();

        return view;
    }

    private void delete() {
        //For delete the user by admin using Right swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");


                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Users users = snapshot.getValue(Users.class);
                            if (users.getFirst_name().equals(userlist.get(position).getFirst_name())
                                    && users.getLast_name().equals(userlist.get(position).getLast_name())
                                    && users.getUser_email().equals(userlist.get(position).getUser_email())) {

                                //confirmation for delete the user
                                alretdialog = new android.app.AlertDialog.Builder(getActivity());
                                alretdialog.setTitle("Delete the user");
                                alretdialog.setMessage("Are you sure you want to delete " + users.getFirst_name() + " " + users.getLast_name() + "?");
                                alretdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference.child(snapshot.getKey()).removeValue();
                                        Log.d(TAG, "Deleted .");
                                        Toast.makeText(getActivity(), "Deleted the User.", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                alretdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alretdialog.create();
                                alretdialog.show();


                            }
                        }
                        userAdapter.notifyDataSetChanged();

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
                p.setColor(ContextCompat.getColor(getActivity(), R.color.colorred));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, p);
                c.clipRect(background);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trash);
                RectF icon_dest = new RectF((float) itemView.getLeft() + imageWidth, (float) itemView.getTop() + ((height / 2) - (imageWidth / 2)), (float) itemView.getLeft() + 2 * imageWidth, (float) itemView.getBottom() - ((height / 2) - (imageWidth / 2)));
                c.drawBitmap(bitmap, null, icon_dest, p);
                c.restore();

            }

        }).attachToRecyclerView(binding.userRecyclerview);

    }

    private void readUser() {
        final ProgressDialog Dialog = new ProgressDialog(getContext());
        Dialog.setMessage("Please wait ...... ");
        Dialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Dialog.dismiss();
                    Users users = dataSnapshot1.getValue(Users.class);
                    userlist.add(users);
                    Log.d(TAG, "User:" + userlist);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void searchuser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("first_name")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Users users = snapshot.getValue(Users.class);
                    userlist.add(users);
                    Log.d(TAG, "userquery:" + userlist);

                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
