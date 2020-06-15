package com.example.nirmol_nogori.Trash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.ActivityNewsNdTrendAdminBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class News_nd_Trend_Admin extends AppCompatActivity implements View.OnClickListener {
    private ActivityNewsNdTrendAdminBinding binding;
    private static final String TAG = "News_nd_Trend_Admin";
    private DatabaseReference databaseReference;
    private int image_rec_code = 1;
    private FirebaseAuth firebaseAuth;
    private Uri filepath_uri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private DatePickerDialog datePickerDialog;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsNdTrendAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageReference = FirebaseStorage.getInstance().getReference("News_Trend");
        databaseReference = FirebaseDatabase.getInstance().getReference("News_Trend");
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(News_nd_Trend_Admin.this);

        binding.saveNews.setOnClickListener(this);
        binding.newsphoto.setOnClickListener(this);
        binding.newsdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.newsphoto) {
            openGallery();
        }

        if (v == binding.saveNews) {

            if (filedchecking()) {
                NewsInsert();
            }
        }

        if (v == binding.newsdate) {
            date();
        }
    }


    //Date insert
    private void date() {
        Calendar mcurrentDate = Calendar.getInstance();
        int currentDay = 0;
        int currentYear = 0;
        int currentMonth = 0;
        if (currentYear == 0 || currentMonth == 0 || currentDay == 0) {

            currentYear = mcurrentDate.get(Calendar.YEAR);

            currentMonth = mcurrentDate.get(Calendar.MONTH);
            currentDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        }
        datePickerDialog = new DatePickerDialog(News_nd_Trend_Admin.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        binding.newsdate.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();
    }


    //Galary open for place picture
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, image_rec_code);
    }

    //check all field are empty or not
    private boolean filedchecking() {
        if (!binding.newstitle.getText().toString().isEmpty()
                && !binding.newsdate.getText().toString().isEmpty()
                && !binding.newssrc.getText().toString().isEmpty()
                && filepath_uri != null) {
            return true;
        } else {
            Toast.makeText(this, "Please fill the all Informations and Image", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //News Place
    public void NewsInsert() {
        final String userid = "WM1vIUC6esTbafyXAE69UvwTDLU";
        if (filepath_uri != null) {
            progressDialog.setTitle("Insert the news...");
            progressDialog.show();
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtention(filepath_uri));

            storageReference2.putFile(filepath_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String name = binding.newstitle.getText().toString();
                                    String newsDate = binding.newsdate.getText().toString().trim();
                                    String src = binding.newssrc.getText().toString().toLowerCase();
                                    String url = uri.toString();

                                    progressDialog.dismiss();

                                    Toast.makeText(News_nd_Trend_Admin.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "done");

                                    String uplodeid = databaseReference.push().getKey();
                                    News news = new News(userid, name, src, newsDate, url);
                                    databaseReference.child(uplodeid).setValue(news);
                                    Log.d(TAG, "done" + url);

                                    afterRegistrationofClener();

                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "" + e.getMessage());
                    Toast.makeText(News_nd_Trend_Admin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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
            Picasso.get().load(filepath_uri).into(binding.newsphoto);

//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath_uri);
//                binding.newsphoto.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }

    }

    //set all filed null
    private void afterRegistrationofClener() {
        binding.newstitle.setText("");
        binding.newssrc.setText("");
        binding.newsdate.setText("");
        filepath_uri = null;

        Picasso.get().load(R.drawable.adduserphoto).into(binding.newsphoto);
        binding.newstitle.setHint("News");
        binding.newssrc.setHint("Source");
        binding.newsdate.setHint("News Date");

    }

}
