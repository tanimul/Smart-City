package com.example.nirmol_nogori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView textView_go_registration;
    private Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView_go_registration = findViewById(R.id.textView_from_login_registration);
        button_login=findViewById(R.id.button_login);
        textView_go_registration.setOnClickListener(this);
        button_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_login) {
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Login.this, Registration.class);
            startActivity(intent);
        }

    }
}
