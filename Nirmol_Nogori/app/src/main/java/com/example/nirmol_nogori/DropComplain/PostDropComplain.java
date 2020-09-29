package com.example.nirmol_nogori.DropComplain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.nirmol_nogori.Fragment.HomeDropComplainFragment;
import com.example.nirmol_nogori.Model.Complain;
import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.Model.Repost;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;

import com.example.nirmol_nogori.databinding.ActivityPostDropComplainBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class PostDropComplain extends AppCompatActivity {
    private ActivityPostDropComplainBinding binding;
    private static final int LOCATION_REQUEST_CODE = 0;
    private static final String TAG = "Drop_Complain_Post";
    private int image_rec_code = 1;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Uri filepath_uri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    boolean request = false;
    private PlacesClient placesClient;
    private Place place;
    private String location;
    private String fullname;
   boolean imagereq = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDropComplainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Bundle complainid = getIntent().getExtras();

        if (complainid != null) {
            request = complainid.getBoolean("edit_request");
            Log.d(TAG, "" + request);

            if (request == false) {
                binding.toolbarTitle.setText("Repost complain");
                binding.postcomplainsubmit.setText("REPOST");
                binding.edittextcomplain.setHint("Describe your repost...");
                binding.complainlocation.setVisibility(View.GONE);
                storageReference = FirebaseStorage.getInstance().getReference("Repost");
                databaseReference = FirebaseDatabase.getInstance().getReference("Repost");
            } else {
                binding.toolbarTitle.setText("Edit complain");
                binding.postcomplainsubmit.setText("SAVE");
                binding.edittextcomplain.setText("" + complainid.getString("edit_request_details"));
                Picasso.get().load(complainid.getString("edit_request_complainimage")).into(binding.imageforcompalain);
                binding.closecomplainimage.setVisibility(View.VISIBLE);
                binding.area.setVisibility(View.VISIBLE);
                binding.complainlocation.setVisibility(View.GONE);
                binding.closearea.setVisibility(View.VISIBLE);
                binding.addphotoforcomplain.setVisibility(View.GONE);
                location = complainid.getString("edit_request_area");
                binding.area.setText("-at " + complainid.getString("edit_request_area"));
                storageReference = FirebaseStorage.getInstance().getReference("Complains");
                databaseReference = FirebaseDatabase.getInstance().getReference("Complains");

            }


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
                imagereq = false;
            }
        });

        binding.closearea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = null;
                binding.area.setVisibility(View.GONE);
                binding.closearea.setVisibility(View.GONE);
                binding.complainlocation.setVisibility(View.VISIBLE);
            }
        });

        binding.complainlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAutoComplete(LOCATION_REQUEST_CODE);
            }
        });


        binding.postcomplainsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complainid != null) {
                    String date = complainid.getString("edit_request_date");
                    String username = complainid.getString("edit_request_usrname");
                    if (request == true) {
                        String complain_id = complainid.getString("edit_request_complainid");
                        complain_edit(complain_id, Uri.parse(complainid.getString("edit_request_complainimage")), location, date, username);
                    } else {
                        String complain_id = complainid.getString("complain_id");
                        repost(complain_id);
                    }
                } else {
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

    private void complain_edit(final String complain_id, final Uri edfilepath_uri, final String area, final String date, final String username) {
        Log.d(TAG, "Complain Edit id " + complain_id);

        final String details = binding.edittextcomplain.getText().toString();

        if (filepath_uri != null && area != null && !TextUtils.isEmpty(details)) {
            progressDialog.setTitle("Updating the Complain...");
            progressDialog.show();
            final StorageReference storageReference3 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtention(filepath_uri));


            storageReference3.putFile(filepath_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();

                                    progressDialog.dismiss();

                                    Toast.makeText(PostDropComplain.this, "Successfully saved", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Update Done.");

                                    Complain complain = new Complain(area, complain_id, url, date
                                            , details, firebaseAuth.getCurrentUser().getUid(), username);
                                    databaseReference.child(date).child(username).child(complain_id).setValue(complain);

                                    Intent intent = new Intent(PostDropComplain.this, DropComplain.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

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
        } else {
            if (imagereq == true) {

                progressDialog.setTitle("Updating the Complain...");
                progressDialog.show();
                if (!TextUtils.isEmpty(details) && edfilepath_uri != null && area != null) {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("area", area);
                    hashMap.put("details", details);

                    databaseReference.child(date).child(username).child(complain_id).updateChildren(hashMap);

                    Toast.makeText(PostDropComplain.this, "Successfully saved", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PostDropComplain.this, DropComplain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progressDialog.dismiss();
                    Log.d(TAG, "Update Done.");
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(PostDropComplain.this, "Please Put the all Informations ", Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(PostDropComplain.this, "Please Put the all Informations ", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void complain(final String userid) {
        Log.d(TAG, "Complain area:" + location);

        final String complaindetails = binding.edittextcomplain.getText().toString();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH) + 1;
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        final String cal = currentYear + "-" + currentMonth + "-" + currentDay;

        Log.d(TAG, "Calender:" + cal);
        Log.d(TAG, "Calender:" + location);


        if (filepath_uri != null && !TextUtils.isEmpty(complaindetails) && !location.equals(null)) {
            progressDialog.setTitle("Insert the Complain...");
            progressDialog.show();
            final StorageReference storageReference4 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtention(filepath_uri));

            storageReference4.putFile(filepath_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    progressDialog.dismiss();
                                    Toast.makeText(PostDropComplain.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "complain added.");

                                    String complainid = databaseReference.push().getKey();
                                    Complain complain = new Complain(location, complainid, url, cal, complaindetails, userid, fullname);
                                    databaseReference.child(cal).child(fullname).child(complainid).setValue(complain);
                                    Log.d(TAG, "done" + url);
                                    Intent intent = new Intent(PostDropComplain.this, DropComplain.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
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
        } else {
            Toast.makeText(PostDropComplain.this, "Please Put the all Informations ", Toast.LENGTH_SHORT).show();
        }

    }

    private void repost(final String complain_id) {
        Log.d(TAG, "Complain repost");
        final String repostdetails = binding.edittextcomplain.getText().toString();

        if (filepath_uri != null && !repostdetails.equals(null)) {
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

                                    String url = uri.toString();

                                    progressDialog.dismiss();

                                    Toast.makeText(PostDropComplain.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Repost Done.");

                                    String repostid = databaseReference.push().getKey();
                                    Repost repost = new Repost(repostdetails, url, firebaseAuth.getCurrentUser().getUid(), repostid, complain_id);
                                    databaseReference.child(complain_id).child(repostid).setValue(repost);
                                    Log.d(TAG, "done" + url);

//                                  getSupportFragmentManager().beginTransaction().replace(R.id.dropcomplain_fragment_container, new HomeDropComplainFragment()).commit();

                                    Intent intent = new Intent(PostDropComplain.this, DropComplain.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
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
        } else {
            Toast.makeText(PostDropComplain.this, "Put the repost image and description ", Toast.LENGTH_SHORT).show();
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
                fullname = users.getFirst_name() + " " + users.getLast_name();
                binding.username.setText(fullname);
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
            imagereq = true;
            Picasso.get().load(filepath_uri).into(binding.imageforcompalain);
            binding.addphotoforcomplain.setVisibility(View.GONE);
            binding.closecomplainimage.setVisibility(View.VISIBLE);
            binding.imageforcompalain.setVisibility(View.VISIBLE);
        }
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK) {
            place = Autocomplete.getPlaceFromIntent(data);
            binding.area.setVisibility(View.VISIBLE);
            binding.closearea.setVisibility(View.VISIBLE);
            binding.area.setText("-at " + place.getName());
            binding.complainlocation.setVisibility(View.GONE);
            location = place.getName();
        }

    }

    private void initAutoComplete(int CALLER_REQUEST_CODE) {

        initPlacesAPI();

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG,
                Place.Field.TYPES);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("bd")
                .setHint("Search for Places")
                .build(this);
        startActivityForResult(intent, CALLER_REQUEST_CODE);
    }

    private void initPlacesAPI() {
        try {
            if (!com.google.android.libraries.places.api.Places.isInitialized()) {
                com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), "AIzaSyBt5IftoRXNGG2kqGb2aWSiZ8GlWrDTeMQ");
            }
            placesClient = Places.createClient(this);
        } catch (Exception e) {
            Log.d(TAG, "initPlacesAPI: error" + e.getMessage());
        }
    }


}
