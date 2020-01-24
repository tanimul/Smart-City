package com.example.nirmol_nogori;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Registration extends AppCompatActivity implements View.OnClickListener {
private Button buttonregistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        buttonregistration=findViewById(R.id.button_registration);
        buttonregistration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Registration Successfully", Toast.LENGTH_SHORT).show();
    }
}
