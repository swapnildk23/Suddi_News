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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registration extends AppCompatActivity {
        AppCompatButton btn_reg;
        EditText firstName,lastName,emailAddress;
        FirebaseAuth mAuth;
        FirebaseDatabase mDatabase;
        DatabaseReference databaseReference;
        FirebaseUser currentUser;
        String firstNameTxt,lastNameTxt,emailTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        BindUI();
        mAuth = FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        databaseReference=mDatabase.getReference();
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(firstName.getText().toString()))
                {
                    Toast.makeText(registration.this, "Please Enter Your First Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(lastName.getText().toString())) {
                    Toast.makeText(registration.this, "Please Enter Your Last Name", Toast.LENGTH_SHORT).show();
                }
                else {
                    firstNameTxt=firstName.getText().toString();
                    lastNameTxt=lastName.getText().toString();
                    if(!TextUtils.isEmpty(emailAddress.getText().toString()))
                    {
                        emailTxt=emailAddress.getText().toString();
                        if(isValidEmail(emailTxt))
                        {
                            if(currentUser!=null)
                            {
                                final String userID = currentUser.getUid();
                                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.hasChild(userID)){
                                        databaseReference.child("Users").child(userID).child("firstName").setValue(firstNameTxt);
                                        databaseReference.child("Users").child(userID).child("lastName").setValue(lastNameTxt);
                                        databaseReference.child("Users").child(userID).child("email").setValue(emailTxt);
                                        Toast.makeText(getApplicationContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(registration.this,News.class));
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(registration.this,News.class));
                                    }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(registration.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        final String userID = currentUser.getUid();
                        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.hasChild(userID)){
                                    databaseReference.child("Users").child(userID).child("firstName").setValue(firstNameTxt);
                                    databaseReference.child("Users").child(userID).child("lastName").setValue(lastNameTxt);
                                    Toast.makeText(getApplicationContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(registration.this,News.class));
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(registration.this,News.class));
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

    private void BindUI()
    {
        btn_reg=findViewById(R.id.btn_registration);
        firstName=findViewById(R.id.reg_user_first_name);
        lastName=findViewById(R.id.reg_user_last_name);
        emailAddress=findViewById(R.id.reg_email_optional);
    }
    private boolean isValidEmail(String email) {
        // Regular expression for validating an email address
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}