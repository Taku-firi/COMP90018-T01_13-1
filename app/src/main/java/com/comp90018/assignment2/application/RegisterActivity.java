package com.comp90018.assignment2.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.comp90018.assignment2.R;
import com.comp90018.assignment2.application.objects.Event;
import com.comp90018.assignment2.application.objects.User;
import com.comp90018.assignment2.application.utils.DaoUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        MaterialButton reg = findViewById(R.id.registerBtn);
        TextView haveAccount = findViewById(R.id.haveAccount);
        TextInputEditText etUsername = findViewById(R.id.username);
        TextInputEditText etEmail = findViewById(R.id.email);
        TextInputEditText etPassword = findViewById(R.id.password);
        TextInputEditText etConfirm = findViewById(R.id.confirmPassword);

        DaoUser daoUser = new DaoUser();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password= etPassword.getText().toString();
                String confirm = etConfirm.getText().toString();

                if (username.isEmpty()|| email.isEmpty()|| password.isEmpty()|| confirm.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
                else if (!password.equals(confirm)){
                    Toast.makeText(RegisterActivity.this,"Passwords are not matching",Toast.LENGTH_SHORT).show();

                }

                else{
                    User cUser = new User(username,email);
                    cUser.setPassword(password);

                    daoUser.getDatabaseReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(email)){
                                Toast.makeText(RegisterActivity.this,"This e-mail already has a corresponding user",Toast.LENGTH_SHORT).show();
                            }else {
                                daoUser.add(cUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(RegisterActivity.this,"User inserted",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this,"Error inserting"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }



            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }
}