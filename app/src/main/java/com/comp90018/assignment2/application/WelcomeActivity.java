package com.comp90018.assignment2.application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


import androidx.appcompat.app.AppCompatActivity;

import com.comp90018.assignment2.R;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcomepage_layout);
        getSupportActionBar().hide();

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                finish();
            }
        };
        handler.postDelayed(runnable,3000);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
