package com.example.suddinews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class registration extends AppCompatActivity {
        AppCompatButton btn_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

    }
    public void gotohome(View view)
    {
        final Intent i=new Intent(this,News.class);
        startActivity(i);
    }
    private void BindUI()
    {
        btn_reg=findViewById(R.id.btn_reg);
    }
}