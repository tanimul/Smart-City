package com.example.nirmol_nogori;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_go_login, button_signout;
    private TextView textView_go_registration;
    private SignInButton signInButton_google;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private int RC_sign_in = 1;
    private long backpressed;
    private Toast backtost;
    private CallbackManager callbackManager;
    private LoginButton login_facebook;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "fff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_go_login = findViewById(R.id.button_go_Login);
        textView_go_registration = findViewById(R.id.textView_from_main_registration);
        signInButton_google = findViewById(R.id.signin_google);
        button_signout = findViewById(R.id.button_signout);
        login_facebook = findViewById(R.id.login_facebook_button);

        firebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        login_facebook.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        button_go_login.setOnClickListener(this);
        textView_go_registration.setOnClickListener(this);
        button_signout.setOnClickListener(this);
        signInButton_google.setOnClickListener(this);
        login_facebook.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (R.id.button_go_Login == v.getId()) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        } else if (R.id.textView_from_main_registration == v.getId()) {
            Intent intent = new Intent(MainActivity.this, Registration.class);
            startActivity(intent);
        } else if (R.id.button_signout == v.getId()) {
            signOut();
        } else if (R.id.signin_google == v.getId()) {
            signIn_google();
        } else if (R.id.login_facebook_button == v.getId()) {
            signIn_facebook();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_sign_in) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSigninResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void updateUI(FirebaseUser firebaseUser) {


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (account != null) {
            button_signout.setVisibility(View.VISIBLE);
            String personname = account.getDisplayName();
            String personemail = account.getEmail();
            String personid = account.getId();
            Toast.makeText(this, personemail + "\n" + personname + "\n" + personid, Toast.LENGTH_SHORT).show();
        }
        if (firebaseUser != null) {
            Toast.makeText(this, "You're logged in", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You're not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            //  firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void signIn_google() {

        Intent signin_intent = googleSignInClient.getSignInIntent();
        startActivityForResult(signin_intent, RC_sign_in);

    }

    private void signIn_facebook() {

        login_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handlefac_SigninResult(loginResult.getAccessToken());
                Toast.makeText(MainActivity.this, "on sunccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "on cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "myerror" + error);
                Toast.makeText(MainActivity.this, "on error", Toast.LENGTH_SHORT).show();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    updateUI(currentUser);
                } else {
                    updateUI(null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (accessTokenTracker == null) {
                    firebaseAuth.signOut();
                }
            }
        };

    }

    private void signOut() {

        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseAuth.signOut();
                        button_signout.setVisibility(View.INVISIBLE);
                    }
                });

    }

    private void handleSigninResult(Task<GoogleSignInAccount> completedtask) {

        try {

            GoogleSignInAccount acc = completedtask.getResult(ApiException.class);
            Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);

        } catch (ApiException e) {

            Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);

        }

    }

    private void handlefac_SigninResult(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_SHORT).show();
                    updateUI(user);
                } else {
                    Toast.makeText(MainActivity.this, "failed ", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    firebaseAuth.signOut();
                }
            }
        };
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {

        try {

            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                }
            });

        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {

        if (backpressed + 2000 > System.currentTimeMillis()) {
            backtost.cancel();
            super.onBackPressed();
            return;
        } else {
            backtost = Toast.makeText(MainActivity.this, "press back again to Exit", Toast.LENGTH_SHORT);
            backtost.show();
        }

        backpressed = System.currentTimeMillis();

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }


}
