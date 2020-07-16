package com.example.nirmol_nogori.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.nirmol_nogori.Model.News;
import com.example.nirmol_nogori.NewsAndTrend.News_nd_Trend;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.databinding.FragmentNewsAndTrendAdminBinding;
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

import static android.app.Activity.RESULT_OK;

public class News_and_Trend_Admin_Fragment extends Fragment implements View.OnClickListener {
    private FragmentNewsAndTrendAdminBinding binding;
    private static final String TAG = "News_and_Trend_Adm_Frag";
    private DatabaseReference databaseReference;
    private int image_rec_code = 1;
    private FirebaseAuth firebaseAuth;
    private Uri filepath_uri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private DatePickerDialog datePickerDialog;
    private StorageTask mUploadTask;


    public News_and_Trend_Admin_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsAndTrendAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("News_Trend");
        databaseReference = FirebaseDatabase.getInstance().getReference("News_Trend");

        progressDialog = new ProgressDialog(getActivity());

        binding.saveNews.setOnClickListener(this);
        binding.newsphoto.setOnClickListener(this);
        binding.newsdate.setOnClickListener(this);
        binding.shownewstrend.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == binding.newsphoto) {
            openGallery();
        }

        if (v == binding.newsdate) {
            date();
        }

        if (v == binding.saveNews) {

            if (filedchecking()) {
                adminid();
            }
        }
        if(v==binding.shownewstrend){
            Intent intent=new Intent(getContext(), News_nd_Trend.class);
            intent.putExtra("news_trend_admin_request","news_trend_admin_request");
            getActivity().startActivity(intent);
        }


    }

    private void adminid() {
        final Bundle admininformation = getActivity().getIntent().getExtras();
        if (admininformation != null) {

            String adminid = admininformation.getString("adminid");
            Log.d(TAG, "" + adminid);

            NewsInsert(adminid);
        }


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
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String monthofyear = "" + month;
                        String dayofmonth = "" + dayOfMonth;
                        if (month < 10) {
                            monthofyear = "0" + month;
                        }
                        if (dayOfMonth < 10) {
                            dayofmonth = "0" + dayOfMonth;
                        }
                        binding.newsdate.setText(year + "-" + monthofyear + "-" + dayofmonth);
                    }
                }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();
    }


    //News Place
    public void NewsInsert(String userid) {

        final String adminid = userid;

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
                                    String newstitle = binding.newstitle.getText().toString();
                                    String newsDate = binding.newsdate.getText().toString().trim();
                                    String src = binding.newssrc.getText().toString().toLowerCase();
                                    String url = uri.toString();

                                    progressDialog.dismiss();

                                    Toast.makeText(getActivity(), "Successfully added", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "done");

                                    String uplodeid = databaseReference.push().getKey();
                                    News news = new News(adminid, newsDate, url, newstitle, src);
                                    databaseReference.child(newsDate).child(uplodeid).setValue(news);
                                    Log.d(TAG, "done" + url);

                                    afterRegistrationofClener();

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

    //check all field are empty or not
    private boolean filedchecking() {
        if (!binding.newstitle.getText().toString().isEmpty()
                && !binding.newsdate.getText().toString().isEmpty()
                && !binding.newssrc.getText().toString().isEmpty()
                && filepath_uri != null) {
            return true;
        } else {
            Toast.makeText(getActivity(), "Please fill the all Informations and Image", Toast.LENGTH_SHORT).show();
            return false;
        }
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
