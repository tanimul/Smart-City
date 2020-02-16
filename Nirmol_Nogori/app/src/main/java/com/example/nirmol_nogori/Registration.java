package com.example.nirmol_nogori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private Button buttonregistration;
    private TextView go_login_from_Reg;
    private EditText f_name, l_name, u_email, u_password, u_con_password;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        buttonregistration = findViewById(R.id.button_registration);
        f_name = findViewById(R.id.edittext_firstname);
        l_name = findViewById(R.id.edittext_lastname);
        u_email = findViewById(R.id.edittext_registration_email);
        u_password = findViewById(R.id.edittext_registration_password);
        u_con_password = findViewById(R.id.edittext_registration_con_password);
        go_login_from_Reg = findViewById(R.id.textView_login_from_registration);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        buttonregistration.setOnClickListener(this);
        go_login_from_Reg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.textView_login_from_registration) {
            Intent intent = new Intent(Registration.this, Login.class);
            startActivity(intent);

        } else {
            userRegistration();
        }

    }

    String first_name, last_name, user_email, user_password, user_con_password;

    public void userRegistration() {

        first_name = f_name.getText().toString().trim();
        last_name = l_name.getText().toString().trim();
        user_email = u_email.getText().toString().trim();
        user_password = u_password.getText().toString().trim();
        user_con_password = u_con_password.getText().toString().trim();

        userRegistrationValidation();

        firebaseAuth.createUserWithEmailAndPassword(user_email, user_password)
                .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(first_name, last_name, user_email);
                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Registration.this, "Registration complete", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(Registration.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    public void userRegistrationValidation() {

        if (first_name.isEmpty()) {
            f_name.setError("Enter a First name please");
            f_name.requestFocus();
            return;
        }

        if (last_name.isEmpty()) {

            l_name.setError("Enter a Last name please");
            l_name.requestFocus();
            return;

        }

        if (user_email.isEmpty()) {

            u_email.setError("Enter a Email please");
            u_email.requestFocus();
            return;

        }

        if (user_password.isEmpty()) {

            u_password.setError("Enter a Password please");
            u_password.requestFocus();
            return;

        }

        if (user_con_password.isEmpty()) {

            u_con_password.setError("Enter a Confirm Password please");
            u_con_password.requestFocus();
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(user_email).

                matches()) {

            u_email.setError("Enter a Valid Email please");
            u_email.requestFocus();
            return;

        }

        if (user_password.length() < 6) {
            u_password.setError("Enter a atleast 6 digit Password please");
            u_password.requestFocus();
            return;
        }
        if (user_con_password.length() < 6) {
            u_con_password.setError("Enter a atleast 6 digit Password please");
            u_con_password.requestFocus();
            return;
        }
        if (!user_password.equals(user_con_password)) {
            u_password.setError("Password and Confirm Password must be same");
            u_password.requestFocus();
            return;

        }

    }

}

