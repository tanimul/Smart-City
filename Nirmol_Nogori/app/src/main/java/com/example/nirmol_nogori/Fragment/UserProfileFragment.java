package com.example.nirmol_nogori.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.nirmol_nogori.Adapter.PhotoAdapter;
import com.example.nirmol_nogori.DoorToDoor.CleanerProfile;
import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.FragmentProfileAdminBinding;
import com.example.nirmol_nogori.databinding.FragmentUserProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding binding;
    private int image_rec_code = 1;
    private Uri filepath_uri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    String profileid;
    private static final String TAG = "User_Profile_Fragment";

    RecyclerView recyclerView;
    PhotoAdapter myphotoAdapter;
    List<Complain> mpostlist;

    private List<String> mysaves;
    RecyclerView recyclerView_saves;
    PhotoAdapter complain_photo_saved_adapter;
    List<Complain> complainlist_saves;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("complainerid", "none");

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (profileid.equals(userid)) {
            updateprofilefiled(userid);
            Log.d("dddddd", "user:" + userid);
        } else {
            Log.d("dddddd", "profile:" + profileid);
            updateprofilefiled(profileid);
            binding.savedcomplain.setVisibility(View.GONE);
        }


        binding.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Choose your Profile picture", Toast.LENGTH_SHORT).show();
                openGallery();
            }
        });

        binding.savedcomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.recyclerViewComplain.setVisibility(View.GONE);
                binding.recyclerViewComplainSave.setVisibility(View.VISIBLE);
            }
        });
        binding.mycomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.recyclerViewComplain.setVisibility(View.VISIBLE);
                binding.recyclerViewComplainSave.setVisibility(View.GONE);
            }
        });


        recyclerView = view.findViewById(R.id.recycler_view_complain);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(linearLayoutManager);
        mpostlist = new ArrayList<>();
        myphotoAdapter = new PhotoAdapter(getContext(), mpostlist);
        recyclerView.setAdapter(myphotoAdapter);


        recyclerView_saves = view.findViewById(R.id.recycler_view_complain_save);
        recyclerView_saves.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new GridLayoutManager(getContext(), 3);
        recyclerView_saves.setLayoutManager(linearLayoutManager1);
        complainlist_saves = new ArrayList<>();
        complain_photo_saved_adapter = new PhotoAdapter(getContext(), complainlist_saves);
        recyclerView_saves.setAdapter(complain_photo_saved_adapter);


        recyclerView_saves.setVisibility(View.GONE);


        complainsaves();
        mycomplain();

        return view;
    }

    //Galary open for place picture
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, image_rec_code);
    }

    //Get image extention
    public String GetFileExtention(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void updateprofilefiled(String userid) {
        String usesid = userid;
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(usesid);
        databaseReference2.keepSynced(true);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                binding.textviewUserName.setText(users.getFirst_name() + " " + users.getLast_name());
                binding.textviewUseremail.setText(users.getUser_email());
                binding.textviewUserphone.setText(users.getPhoneno());
                Picasso.get().load(users.getUser_image_url())
                        .placeholder(R.drawable.adduserphoto)
                        .fit()
                        .centerCrop()
                        .into(binding.userPhoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == image_rec_code && resultCode == RESULT_OK && data != null) {
            filepath_uri = data.getData();
            Picasso.get().load(filepath_uri).into(binding.userPhoto);
            updateuserdetails(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

    }

    //User Details Update
    public void updateuserdetails(String userid) {
        final String adminid = userid;
        storageReference = FirebaseStorage.getInstance().getReference("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        if (filepath_uri != null) {
            final ProgressDialog Dialog = new ProgressDialog(getActivity());
            Dialog.setMessage("Updating Profile ...");
            Dialog.show();
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtention(filepath_uri));

            storageReference2.putFile(filepath_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Dialog.dismiss();

                                    String url = uri.toString();

                                    Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "done");

                                    databaseReference.child("user_image_url").setValue("" + url);


                                    updateprofilefiled(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "" + e.getMessage());
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void complainsaves() {
        Log.d("dddd", "complain");
        mysaves = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Saves")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    mysaves.add(dataSnapshot1.getKey());

                }
                readsaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readsaves() {
        Log.d("dddd", "read");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Complains");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complainlist_saves.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Complain complain = snapshot1.getValue(Complain.class);
                            for (String id : mysaves) {
                                if (complain.getComplainid().equals(id)) {
                                    complainlist_saves.add(complain);
                                }
                            }
                        }


                    }
                }
                complain_photo_saved_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void mycomplain() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Complains");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mpostlist.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Complain complain = snapshot1.getValue(Complain.class);
                            if (complain.getUserid().equals(profileid)) {
                                mpostlist.add(complain);
                            }
                        }


                    }
                }
                Collections.reverse(mpostlist);
                myphotoAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
