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

// Activity for new users to register
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

        // After entering their data, check and create the new user
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the metadata entered
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password= etPassword.getText().toString();
                String confirm = etConfirm.getText().toString();

                // Check if all the fields are filled
                if (username.isEmpty()|| email.isEmpty()|| password.isEmpty()|| confirm.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
                // Check if the passwords entered twice are the same
                else if (!password.equals(confirm)){
                    Toast.makeText(RegisterActivity.this,"Passwords are not matching",Toast.LENGTH_SHORT).show();

                }
                else{
                    User cUser = new User(username,email);
                    cUser.setPassword(password);

                    // Connect to the database, and add this new user
                    daoUser.getDatabaseReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Check if the username already exists
                            if (snapshot.hasChild(username)){
                                Toast.makeText(RegisterActivity.this,"This user already exist",Toast.LENGTH_SHORT).show();
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

        // Users can go the the login page if they already have accounts
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }
}