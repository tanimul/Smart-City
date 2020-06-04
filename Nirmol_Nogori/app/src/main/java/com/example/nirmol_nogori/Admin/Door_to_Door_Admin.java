package com.example.nirmol_nogori.Admin;

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
import com.example.nirmol_nogori.databinding.ActivityDoorToDoorAdminBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Door_to_Door_Admin extends AppCompatActivity implements View.OnClickListener {
    private ActivityDoorToDoorAdminBinding binding;
    private static final String TAG = "Door_to_Door_Admin";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private int image_rec_code = 1;
    Uri filepath_uri;
    StorageReference storageReference;
    ProgressDialog progressDialog;

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
            cleanerRegistration();
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

        if (filepath_uri != null) {

            progressDialog.setTitle("Registration Processing");
            progressDialog.show();

            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtentyion(filepath_uri));
            storageReference2.putFile(filepath_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String name = binding.clenername.getText().toString().trim();
                            String phoneNo = binding.clenerphone.getText().toString().trim();
                            String area = binding.cleanerlocation.getText().toString().trim();

                            progressDialog.dismiss();

                            Toast.makeText(Door_to_Door_Admin.this, "Successfully added", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "done");

                            //String imageuplodeid = databaseReference.push().getKey();
                            Cleaner _cleaner = new Cleaner(phoneNo, taskSnapshot.getUploadSessionUri().toString());
                            databaseReference.child(area).child(name).setValue(_cleaner);


                        }
                    });
        }
    }

    //Get image extention
    public String GetFileExtentyion(Uri uri) {
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


}
