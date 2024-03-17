package com.example.suddinews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminlogin extends AppCompatActivity {

    EditText admin_id,admin_password;
    AppCompatButton btn_admin_login;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private boolean internetConnected = false;
    private boolean mainActivityStarted = false;
    CardView crdview;
    private ProgressBar loadingProgressBar;
    private TextView internetTextView;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        BindUI();
        startCheckingInternetConnectivity();

        btn_admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(admin_id.getText().toString()))
                {
                    Toast.makeText(adminlogin.this,"Please Enter a valid Admin ID",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(admin_password.getText().toString())) {
                    Toast.makeText(adminlogin.this,"Please Enter a Valid Password",Toast.LENGTH_SHORT).show();
                }
                else{
                        final String id=admin_id.getText().toString();
                        final String password=admin_password.getText().toString();
                        databaseReference.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(id))
                                {
                                    final String getPassword=snapshot.child(id).getValue(String.class);

                                    if (getPassword.equals(password))
                                    {
                                        Toast.makeText(adminlogin.this,"Successfully Logged In",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(adminlogin.this,newsupload.class));
                                        finish();
                                    }

                                    else {
                                        Toast.makeText(adminlogin.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(adminlogin.this,"Wrong Admin ID",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                }
            }
        });
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
    private void stopCheckingInternetConnectivity() {
        handler.removeCallbacks(runnable);
    }
    private void BindUI()
    {
        admin_id=findViewById(R.id.login_admin_id);
        admin_password=findViewById(R.id.login_admin_password);
        btn_admin_login=findViewById(R.id.btn_admin_login);
        loadingProgressBar=findViewById(R.id.loadingProgressBar2);
        internetTextView=findViewById(R.id.internetTextView2);
        crdview=findViewById(R.id.cardView);

    }
}