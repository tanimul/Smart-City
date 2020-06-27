package com.example.nirmol_nogori.DropComplain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.nirmol_nogori.Fragment.HomeDropComplainFragment;
import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.Model.Repost;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;

import com.example.nirmol_nogori.databinding.ActivityPostDropComplainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class PostDropComplain extends AppCompatActivity {
    private ActivityPostDropComplainBinding binding;
    private static final String TAG = "Drop_Complain_Post";
    private int image_rec_code = 1;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Uri filepath_uri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDropComplainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Bundle complainid = getIntent().getExtras();
        if (complainid != null) {
            binding.toolbarTitle.setText("Repost complain");
            binding.postcomplainsubmit.setText("Repost");
            binding.edittextcomplain.setHint("Describe your repost...");
            binding.complainlocation.setVisibility(View.GONE);
            storageReference = FirebaseStorage.getInstance().getReference("Repost");
            databaseReference = FirebaseDatabase.getInstance().getReference("Repost");

        } else {
            storageReference = FirebaseStorage.getInstance().getReference("Complains");
            databaseReference = FirebaseDatabase.getInstance().getReference("Complains");
        }

        progressDialog = new ProgressDialog(PostDropComplain.this);

        firebaseAuth = FirebaseAuth.getInstance();

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        updateprofilepic(userid);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostDropComplain.this, DropComplain.class));
                finish();
            }
        });

        binding.closecomplainimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageforcompalain.setVisibility(View.GONE);
                binding.closecomplainimage.setVisibility(View.GONE);
                binding.addphotoforcomplain.setVisibility(View.VISIBLE);
            }
        });

        binding.complainlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.postcomplainsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complainid != null) {
                    String complain_id = complainid.getString("complain_id");
                    repost(complain_id);
                }else {
                    complain(userid);
                }
            }
        });

        binding.addphotoforcomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void complain(String userid) {

    }

    private void repost(final String complain_id) {
        final String complainId = complain_id;

        if (filepath_uri != null) {
            progressDialog.setTitle("Insert the Repost...");
            progressDialog.show();
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtention(filepath_uri));

            storageReference2.putFile(filepath_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String repostdetails = binding.edittextcomplain.getText().toString();
                                    String url = uri.toString();

                                    progressDialog.dismiss();

                                    Toast.makeText(PostDropComplain.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Repost Done.");

                                    String repostid = databaseReference.push().getKey();
                                    Repost repost = new Repost(repostdetails, url, firebaseAuth.getCurrentUser().getUid());
                                    databaseReference.child(complainId).child(repostid).setValue(repost);
                                    Log.d(TAG, "done" + url);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.dropcomplain_fragment_container, new HomeDropComplainFragment()).commit();

                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "" + e.getMessage());
                    Toast.makeText(PostDropComplain.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void updateprofilepic(String userid) {
        String usesid = userid;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(usesid);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                binding.username.setText(users.getFirst_name() + " " + users.getLast_name());
                Picasso.get().load(users.getUser_image_url())
                        .placeholder(R.drawable.ic_user)
                        .fit()
                        .centerCrop()
                        .into(binding.userPhoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Galary open for place picture
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, image_rec_code);
    }

    //Get image extention
    public String GetFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == image_rec_code && resultCode == RESULT_OK && data != null) {
            filepath_uri = data.getData();
            Picasso.get().load(filepath_uri).into(binding.imageforcompalain);
            binding.addphotoforcomplain.setVisibility(View.GONE);
            binding.closecomplainimage.setVisibility(View.VISIBLE);
            binding.imageforcompalain.setVisibility(View.VISIBLE);
        }

    }

}
