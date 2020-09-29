package com.example.nirmol_nogori.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nirmol_nogori.NewsAndTrend.News_nd_Trend;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.Ui.Home_Menu;
import com.example.nirmol_nogori.databinding.FragmentProfileAdminBinding;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Admin_Fragment extends Fragment implements View.OnClickListener {
    private FragmentProfileAdminBinding binding;
    private DatabaseReference databaseReference;
    private static final String TAG = "Profile_Admin_Fragment";
    private long lastclicktime = 0;
    public static String admin_id=null;

    public Profile_Admin_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        final Bundle admininformation = getActivity().getIntent().getExtras();
        if (admininformation != null) {

            String adminid = admininformation.getString("adminid");
            String name = admininformation.getString("name");
            String email = admininformation.getString("email");
            String phoneno = admininformation.getString("phoneno");
            String imageurl = admininformation.getString("imageurl");

            admin_id=adminid;

            Log.d(TAG, "" + adminid);
            Log.d(TAG, "" + name);
            Log.d(TAG, "" + email);
            Log.d(TAG, "" + phoneno);
            Log.d(TAG, "" + imageurl);

            informationset(adminid, name, email, phoneno, imageurl);
        }

        binding.usingasauser.setOnClickListener(this);

        return view;
    }

    private void informationset(String adminid, String name, String email, String phonono, String imageurl) {

        binding.textviewAdminName.setText("" + name);
        binding.textviewAdminemail.setText("" + email);
        binding.textviewAdminphone.setText("" + phonono);
        Picasso.get().load(imageurl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(binding.adminPhoto);

    }

    @Override
    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - lastclicktime < 1000) {
            return;
        }
        lastclicktime = SystemClock.elapsedRealtime();

          if(view==binding.usingasauser){
            Intent intent=new Intent(getContext(), Home_Menu.class);
            intent.putExtra("admin_request using as a user","admin_request using as a user");
            intent.putExtra("admin_id",admin_id);
            getActivity().startActivity(intent);
        }

    }
}
