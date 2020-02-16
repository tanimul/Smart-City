package com.example.nirmol_nogori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView textView_go_registration;
    private Button button_login;
    private EditText userEmail, userPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView_go_registration = findViewById(R.id.textView_from_login_registration);
        button_login = findViewById(R.id.button_login);
        userEmail = findViewById(R.id.edittext_login_email);
        userPassword = findViewById(R.id.edittext_login_password);
        mAuth = FirebaseAuth.getInstance();

        textView_go_registration.setOnClickListener(this);
        button_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_login) {
            userLogin();
        } else {
            Intent intent = new Intent(Login.this, Registration.class);
            startActivity(intent);
        }

    }



    private void userLogin() {
        String user_email = userEmail.getText().toString().trim();
        String user_password = userPassword.getText().toString().trim();
        try {
            mAuth.signInWithEmailAndPassword(user_email, user_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "login successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, "login unsuccessfully", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        } catch (Exception e) {
            Toast.makeText(Login.this, "login unsuccessfully", Toast.LENGTH_SHORT).show();
        }


    }
}
