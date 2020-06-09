package com.example.nirmol_nogori.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nirmol_nogori.R;

public class Login_Admin extends AppCompatActivity {

    //Todo admin login just firebase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__admin);

//        //testing door to door admin
//       startActivity(new Intent(Login_Admin.this, Door_to_Door_Admin.class));
//        //testing news nd trend admin
//        startActivity(new Intent(Login_Admin.this, News_nd_Trend_Admin.class));
    }
}
