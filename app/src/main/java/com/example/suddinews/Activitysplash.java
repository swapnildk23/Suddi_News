package com.example.suddinews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
public class Activitysplash extends AppCompatActivity {
    private RelativeLayout ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Call UI binding function
        BindUi();

        final Intent i = new Intent(this, MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle b = ActivityOptions.makeSceneTransitionAnimation(Activitysplash.this).toBundle();
                startActivity(i, b);
                finish();
            }
        }, 1000);
    }

    // Bind UI elements with the respective component variables

    private void BindUi() {
        ly = findViewById(R.id.lay);
    }
}
