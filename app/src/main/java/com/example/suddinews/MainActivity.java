package com.example.suddinews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    AppCompatButton btn_login, btn_next;
    EditText phoneno, otp;
    FirebaseAuth mAuth;
    private ProgressBar loadingProgressBar;
    private TextView internetTextView;
    String verificationId;
    FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;
    private boolean internetConnected = false;
    private boolean mainActivityStarted = false;
    CardView crdview;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindUI();
        startCheckingInternetConnectivity();

        mAuth = FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        databaseReference=mDatabase.getReference();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneno.getText().toString();
                if ((TextUtils.isEmpty(phoneno.getText().toString()))) {
                    Toast.makeText(MainActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (phoneNumber.length() < 10) {
                        Toast.makeText(MainActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                    } else {
                      sendverificationcode(phoneNumber);
                    }
                }

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((TextUtils.isEmpty(otp.getText().toString()))) {
                    Toast.makeText(MainActivity.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifycode(otp.getText().toString());
                }

//                startActivity(new Intent(MainActivity.this, News.class));
            }
        });
    }

    private void sendverificationcode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phoneNumber)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
            Toast.makeText(MainActivity.this,"OTP Sent",Toast.LENGTH_SHORT).show();
            btn_login.setEnabled(true);
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(@NotNull PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifycode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(@NotNull FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                            /*
                            Code for saving phone number and checking and deciding whether to go to registration page or
                            home page should be here and make sure to change the startActivity as required
                             */
                             FirebaseUser currentFireUser = FirebaseAuth.getInstance().getCurrentUser();
                             if (currentFireUser != null) {
                                 // User is signed in, get their user ID
                                 final String userId = currentFireUser.getUid();
                                 Log.d("MainActivity", "User ID: " + userId);
                                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(userId))
                                        {
                                            startActivity(new Intent(MainActivity.this,News.class));
                                            finish();
                                        }
                                        else {
                                            startActivity(new Intent(MainActivity.this,registration.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                         }

                    }
                }
    });
    }
    public void admin(View view)
    {
        Intent j = new Intent(this, adminlogin.class);
        startActivity(j);
    }
    private void startCheckingInternetConnectivity() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                checkInternetConnectivity();
                handler.postDelayed(this, 1000); // Check every second
            }
        };
        handler.postDelayed(runnable, 1000); // Initial delay
    }
    private void stopCheckingInternetConnectivity() {
        handler.removeCallbacks(runnable);
    }

    // Method to check internet connectivity
    private void checkInternetConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                network = connectivityManager.getActiveNetwork();
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            internetConnected = capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            if (internetConnected && !mainActivityStarted) {
                stopCheckingInternetConnectivity(); // Stop checking once internet connected
                crdview.setVisibility(View.VISIBLE);
                loadingProgressBar.setVisibility(View.INVISIBLE);
                internetTextView.setVisibility(View.INVISIBLE);

            } else {
                loadingProgressBar.setVisibility(View.VISIBLE);
                internetTextView.setVisibility(View.VISIBLE);

                crdview.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void BindUI()
    {
        btn_login=findViewById(R.id.btn_login);
        btn_next=findViewById(R.id.btn_next);
        phoneno=findViewById(R.id.login_phone);
        otp=findViewById(R.id.login_otp);
        loadingProgressBar=findViewById(R.id.loadingProgressBar1);
        internetTextView=findViewById(R.id.internetTextView1);
        crdview=findViewById(R.id.crdView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null)
        {
            startActivity(new Intent(MainActivity.this, News.class));
            finish();
        }
    }
}