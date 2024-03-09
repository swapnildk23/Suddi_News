package com.example.suddinews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.mtp.MtpConstants;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    AppCompatButton btn_login,btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindUI();
    }
    public void admin(View view)
    {
        Intent j = new Intent(this, adminlogin.class);
        startActivity(j);
    }
    public void logi(View view)
    {
        Intent i = new Intent(this, News.class);
        startActivity(i);
    }
    private void BindUI()
    {
        btn_login=findViewById(R.id.btn_login);
        btn_next=findViewById(R.id.btn_next);
    }
}