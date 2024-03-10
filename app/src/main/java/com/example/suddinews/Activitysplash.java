package com.example.suddinews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Activitysplash extends AppCompatActivity {
    private RelativeLayout ly;
    private ProgressBar loadingProgressBar;
    private TextView internetTextView;
    private boolean internetConnected = false;
    private boolean mainActivityStarted = false;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Call UI binding function
        BindUi();

        // Start checking internet connectivity
        startCheckingInternetConnectivity();
    }

    // Bind UI elements with the respective component variables
    private void BindUi() {
        ly = findViewById(R.id.lay);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        internetTextView = findViewById(R.id.internetTextView);
    }

    // Method to start checking internet connectivity
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

    // Method to stop checking internet connectivity
    private void stopCheckingInternetConnectivity() {
        handler.removeCallbacks(runnable);
    }

    // Method to check internet connectivity
    private void checkInternetConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            internetConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
            if (internetConnected&&!mainActivityStarted) {
                stopCheckingInternetConnectivity(); // Stop checking once internet connected
                startMainActivity();
            } else {
                loadingProgressBar.setVisibility(View.VISIBLE);
                internetTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    // Method to start the MainActivity
    private void startMainActivity() {
        final Intent i = new Intent(this, MainActivity.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(Activitysplash.this).toBundle();
        startActivity(i, b);
        finish();
        mainActivityStarted=true;
    }
}
