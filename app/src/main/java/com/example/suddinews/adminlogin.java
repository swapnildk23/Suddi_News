package com.example.suddinews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        BindUI();
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
    public void admin_login(View view)
    {
        Intent i =new Intent(this,newsupload.class);
        startActivity(i);
    }
    private void BindUI()
    {
        admin_id=findViewById(R.id.login_admin_id);
        admin_password=findViewById(R.id.login_admin_password);
        btn_admin_login=findViewById(R.id.btn_admin_login);
    }
}