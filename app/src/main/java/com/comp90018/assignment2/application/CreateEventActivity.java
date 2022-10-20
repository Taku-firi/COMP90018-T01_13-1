package com.comp90018.assignment2.application;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.comp90018.assignment2.R;
import com.google.android.material.textview.MaterialTextView;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");

        Double Lat = bundle.getDouble("Latitude");
        Double Long = bundle.getDouble("Longitude");

        MaterialTextView tv1 = findViewById(R.id.tv1);
        MaterialTextView tv2 = findViewById(R.id.tv2);
        tv1.setText(Lat.toString());
        tv2.setText(Long.toString());

    }

}
