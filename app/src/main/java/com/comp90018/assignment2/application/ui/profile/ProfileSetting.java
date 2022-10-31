package com.comp90018.assignment2.application.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.comp90018.assignment2.R;
import com.comp90018.assignment2.application.objects.User;
import com.comp90018.assignment2.application.utils.DaoUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileSetting extends AppCompatActivity {

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setting);
        getSupportActionBar().hide();
        //get the spinner from the xml.
        AppCompatSpinner inGender = findViewById(R.id.editGender);

        TextInputEditText inPwd = findViewById(R.id.editPassword);
        TextInputEditText inAge = findViewById(R.id.editAge);
        TextInputEditText inLoc = findViewById(R.id.editLocation);
        TextInputEditText inInterest = findViewById(R.id.editInterest);
        TextInputEditText inDetail = findViewById(R.id.editSelfIntroduction);

        DaoUser daoUser = new DaoUser();
        SharedPreferences sharedPreferences =this.getSharedPreferences("assignment2",MODE_PRIVATE);
        String username = sharedPreferences.getString("currentUser","");
        daoUser.getDatabaseReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username)){
                    User cUser = snapshot.child(username).getValue(User.class);
                    inPwd.setText(cUser.getPassword());
                    inAge.setText(cUser.getAge());
                    inLoc.setText(cUser.getLocation());
                    inInterest.setText(cUser.getInterest());
                    inDetail.setText(cUser.getDetail());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        MaterialButton btn_submit= findViewById(R.id.settingSubmit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nPassword = inPwd.getText().toString();
                String nGender = inGender.getSelectedItem().toString();
                String nAge = inAge.getText().toString();
                String nLocation = inLoc.getText().toString();
                String nInterest = inInterest.getText().toString();
                String nDetail = inDetail.getText().toString();

                daoUser.getDatabaseReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(username)){
                            User cUser = snapshot.child(username).getValue(User.class);
                            cUser.setPassword(nPassword);
                            cUser.setGender(nGender);
                            cUser.setAge(nAge);
                            cUser.setLocation(nLocation);
                            cUser.setInterest(nInterest);
                            cUser.setDetail(nDetail);
                            daoUser.add(cUser);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                finish();}
        });


    }
}
