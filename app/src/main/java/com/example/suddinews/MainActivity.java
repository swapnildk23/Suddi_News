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
    AppCompatButton btn_login;
    TextView reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindUI();


    }
    public void regi(View view)
    {
        Intent i = new Intent(this, registration.class);
        startActivity(i);
    }
    public void logi(View view)
    {
        Intent i = new Intent(this, News.class);
        startActivity(i);
    }
    private void BindUI()
    {
        btn_login=findViewById(R.id.btn_login);
        reg=findViewById(R.id.register_text);
    }
}