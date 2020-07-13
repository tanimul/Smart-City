package com.example.nirmol_nogori.Ui;

import com.example.nirmol_nogori.Admin.Login_Admin;
import com.example.nirmol_nogori.DoorToDoor.GetReviewFromuser;
import com.example.nirmol_nogori.Model.Users;
import com.example.nirmol_nogori.R;
import com.example.nirmol_nogori.User.Login_User;
import com.example.nirmol_nogori.User.Registration;
import com.example.nirmol_nogori.databinding.ActivityMainBinding;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.FacebookSdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private android.app.AlertDialog.Builder alretdialog;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private int RC_sign_in = 1;
    private long backpressed;
    private Toast backtost;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "MainActivity";
    private static final int FINE_LOCATION_REQUEST_CODE = 11;
    private static final int LOCATION_SETTINGS_REQUEST_CODE = 22;
    public static boolean CLEANER_REQUEST;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (requestlocationpermission()) {
            locationSettingOption();
        }




        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        //Authenticate with Firebase for facebook Login
        FacebookSdk.sdkInitialize(getApplicationContext());
        binding.signinFacebook.setReadPermissions("email", "public_profile");

        callbackManager = CallbackManager.Factory.create();

        //Authenticate with Firebase for google Login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        //Underline some text
        binding.textViewLoginGoAdmin.setPaintFlags(binding.textViewLoginGoAdmin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.textViewFromMainRegistration.setPaintFlags(binding.textViewFromMainRegistration.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        binding.buttonLoginGoUser.setOnClickListener(this);
        binding.textViewFromMainRegistration.setOnClickListener(this);
        binding.signinGoogle.setOnClickListener(this);
        binding.signinFacebook.setOnClickListener(this);
        binding.textViewLoginGoAdmin.setOnClickListener(this);


    }

    //taking  Fine location
    private boolean requestlocationpermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: permission granted.");
                locationSettingOption();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission denied.");
                new AlertDialog.Builder(this)
                        .setTitle("Enable Location Permission")
                        .setMessage("You need to give Location Permission for better user user Experience.")
                        .setPositiveButton("Enable", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        requestlocationpermission();
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void locationSettingOption() {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        Log.d(TAG, "try to loction on ");
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                LOCATION_SETTINGS_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException sendEx) {
                    }
                }
            }
        });

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d(TAG, "onSuccess: location is on.");

                if (firebaseUser != null) {
                    checkUserStatus();
                }
            }
        });
    }


    //check current user status
    private void checkUserStatus() {
        if (firebaseUser != null) {
            if (CLEANER_REQUEST == true) {
                service();
            } else {
                startActivity(new Intent(MainActivity.this, Home_Menu.class));
                finish();
            }
        }
    }

    private void service() {
        alretdialog = new android.app.AlertDialog.Builder(MainActivity.this);
        alretdialog.setTitle("Service");
        alretdialog.setMessage("Have you taken the SERVICE before ?");
        alretdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, GetReviewFromuser.class));
                finish();
            }
        });
        alretdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CLEANER_REQUEST = false;
                checkUserStatus();
                dialog.dismiss();
            }
        });
        alretdialog.create();
        alretdialog.show();

    }

    @Override
    public void onClick(View v) {

        if (v == binding.buttonLoginGoUser) {
            startActivity(new Intent(MainActivity.this, Login_User.class));
        } else if (v == binding.textViewFromMainRegistration) {
            startActivity(new Intent(MainActivity.this, Registration.class));
        } else if (v == binding.signinGoogle) {
            signIn_google();
        } else if (v == binding.signinFacebook) {
            signIn_facebook();
        } else if (v == binding.textViewLoginGoAdmin) {
            startActivity(new Intent(MainActivity.this, Login_Admin.class));
        }

    }

    //##############   Google SIGN IN   ###############//

    private void signIn_google() {
        Intent signin_intent = googleSignInClient.getSignInIntent();
        startActivityForResult(signin_intent, RC_sign_in);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_sign_in) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSigninResult(task);
        }
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Log.d(TAG, "onActivityResult: RESULT_OK");

                    checkUserStatus();
                    break;
                case RESULT_CANCELED:
                    new AlertDialog.Builder(this)
                            .setTitle("Enable Location Services")
                            .setMessage("You need to enable Location Services for better user user Experience.")
                            .setPositiveButton("Enable", new
                                    DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            locationSettingOption();
                                        }
                                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                    Log.d(TAG, "onActivityResult: RESULT_CANCELED");
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleSigninResult(Task<GoogleSignInAccount> completedtask) {

        try {

            GoogleSignInAccount acc = completedtask.getResult(ApiException.class);
            Toast.makeText(MainActivity.this, "successfully logged", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "google Signed in successfully");
            FirebaseGoogleAuth(acc);

        } catch (ApiException e) {
            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "google Signed in failed");
            FirebaseGoogleAuth(null);

        }

    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {

        try {

            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Log.d(TAG, "firebase Signed in successfully");
                        Toast.makeText(MainActivity.this, "successfully logged", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);

                    } else {

                        Log.d(TAG, "firebase Signed in failed");
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);

                    }

                }
            });

        } catch (Exception e) {

        }
    }


    private void updateUI(FirebaseUser firebaseUser) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (account != null) {
            String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            String personname = account.getDisplayName();
            String personemail = account.getEmail();
            String personid = account.getId();
            Toast.makeText(this, personemail + "\n" + personname + "\n" + personid, Toast.LENGTH_SHORT).show();
            Log.d(TAG, personemail + "");


            Users user = new Users(account.getDisplayName(), account.getGivenName(), account.getEmail(), null, null,userid,0);
            databaseReference.child(userid)
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "adding successfully in Database.");
                    checkUserStatus();
                }
            });

        }
        if (firebaseUser != null) {
            Toast.makeText(this, "You're logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Home_Menu.class);
            startActivity(intent);
            finish();

        } else {

            Toast.makeText(this, "You're not logged in", Toast.LENGTH_SHORT).show();

        }
    }

    //##############   Facebook SIGN IN   ###############//

    private void signIn_facebook() {

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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


    }


    private void handlefac_SigninResult(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "facebook Signed in successfully");
                    Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_SHORT).show();
                    updateUI(user);
                } else {
                    Log.d(TAG, "google Signed in failed");
                    Toast.makeText(MainActivity.this, "failed ", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }

    //for Double press for Exit
    @Override
    public void onBackPressed() {

        if (backpressed + 2000 > System.currentTimeMillis()) {
            backtost.cancel();
            super.onBackPressed();
            return;
        } else {
            backtost = Toast.makeText(MainActivity.this, "press BACK again to Exit", Toast.LENGTH_SHORT);
            backtost.show();
        }

        backpressed = System.currentTimeMillis();

    }


}
