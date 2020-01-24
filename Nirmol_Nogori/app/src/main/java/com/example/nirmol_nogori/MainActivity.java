package com.example.nirmol_nogori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_go_login;
    private TextView textView_go_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_go_login = findViewById(R.id.button_go_Login);
        textView_go_registration = findViewById(R.id.textView_from_main_registration);
        button_go_login.setOnClickListener(this);
        textView_go_registration.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (R.id.button_go_Login == v.getId()) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, Registration.class);
            startActivity(intent);
        }

    }
}
