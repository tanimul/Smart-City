package com.example.nirmol_nogori.Trash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.nirmol_nogori.Model.Cleaner;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityDoorToDoorAdminBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Door_to_Door_Admin extends AppCompatActivity implements View.OnClickListener {
    private ActivityDoorToDoorAdminBinding binding;
    private static final String TAG = "Door_to_Door_Admin";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private int image_rec_code = 1;
    private Uri filepath_uri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoorToDoorAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageReference = FirebaseStorage.getInstance().getReference("cleaner_information");
        databaseReference = FirebaseDatabase.getInstance().getReference("Location and Cleaner");
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Door_to_Door_Admin.this);

        binding.saveCleanerInformation.setOnClickListener(this);
        binding.clenerphoto.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == binding.clenerphoto) {
            openGallery();
        }

        if (v == binding.saveCleanerInformation) {
            if (filedchecking()) {
                cleanerRegistration();
            }
        }


    }

    //check all field are empty or not
    private boolean filedchecking() {
        if (!binding.clenername.getText().toString().isEmpty()
                && !binding.cleanerlocation.getText().toString().isEmpty()
                && !binding.clenerphone.getText().toString().isEmpty()
                && filepath_uri != null) {
            return true;
        } else {
            Toast.makeText(this, "Please fill the all Informations and Image", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //Galary open for place picture
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, image_rec_code);
    }

    //Registration of Cleaner
    public void cleanerRegistration() {
        final String publisherid = "WM1vIUC6esTbafyXAE69UvwTDLUED";
        final float rating=4;
        if (filepath_uri != null) {
            progressDialog.setTitle("Registration Processing...");
            progressDialog.show();

            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtention(filepath_uri));
            storageReference2.putFile(filepath_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String name = binding.clenername.getText().toString().trim();
                                    String phoneNo = binding.clenerphone.getText().toString().trim();
                                    String area = binding.cleanerlocation.getText().toString().trim().toLowerCase();
                                    String url=uri.toString();

                                    progressDialog.dismiss();

                                    Toast.makeText(Door_to_Door_Admin.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                    //String uplodeid = databaseReference.push().getKey();
                                    Cleaner _cleaner = new Cleaner(name, phoneNo, url, area, publisherid,rating);
                                    databaseReference.child(area).child(name).setValue(_cleaner);
                                    afterRegistrationofClener();
                                    Log.d(TAG, "done"+url);
                                }
                            });





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, ""+e.getMessage());
                    Toast.makeText(Door_to_Door_Admin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath_uri);
                binding.clenerphoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    //set all filed null
    private void afterRegistrationofClener() {
        binding.clenername.setText("");
        binding.cleanerlocation.setText("");
        binding.clenerphone.setText("");
        filepath_uri = null;

        Picasso.get().load(R.drawable.adduserphoto).into(binding.clenerphoto);
        binding.clenername.setHint("Name");
        binding.cleanerlocation.setHint("Location");
        binding.clenerphone.setHint("Phone");

    }

}
