package com.comp90018.assignment2.application;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.comp90018.assignment2.R;
import com.google.android.material.textview.MaterialTextView;


public class EventDetailActivity extends AppCompatActivity implements SensorEventListener {

    //Dynamically apply for exercise permission
    private static final String[] ACTIVITY_RECOGNITION_PERMISSION = {Manifest.permission.ACTIVITY_RECOGNITION};

    private final String TAG = "TDSSS";
    SensorManager mSensorManager;
    Sensor stepCounter;
    Sensor stepDetector;

    float mSteps = 0;
    MaterialTextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getSupportActionBar().hide();

        // Get SensorManager instance
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        // Check the permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Check whether the permission has been obtained
            int get = ContextCompat.checkSelfPermission(this, ACTIVITY_RECOGNITION_PERMISSION[0]);
            // Check whether the permission is authorized  GRANTED---agree DINIED---cancel
            if (get != PackageManager.PERMISSION_GRANTED) {
                // If this permission is not granted, the system prompts the user to request to enable the permission
                ActivityCompat.requestPermissions(this, ACTIVITY_RECOGNITION_PERMISSION, 321);
            }
        }
        tv = findViewById(R.id.tv_step);
        // Get pedometer sensor data

        stepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepDetector != null){
            // If sensor finds it, register the listener
            mSensorManager.registerListener(this,stepDetector,1000000);
        }
        else{
            Log.e(TAG,"no step Detector sensor found");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ///Prompts the user to manually enable the permission
                    new AlertDialog.Builder(this)
                            .setTitle("Pedometer permission")
                            .setMessage("Unable to obtain pedometer permission")
                            .setPositiveButton("Open now", (dialog12, which) -> {
                                // Jump to the application settings screen
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 123);
                            })
                            .setNegativeButton("Cancel", (dialog1, which) -> {
                                Toast.makeText(getApplicationContext(), "Didn't get permission to run!", Toast.LENGTH_SHORT).show();
                                finish();
                            }).setCancelable(false).show();
                }
            }
        }
    }

    // Implement SensorEventListener callback interface, when the sensor changes, it will call back the interface,
    // and the result will be passed back to the app via event.
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == 1.0f){
            mSteps++;
        }
        Log.i(TAG,"Detected step changes:"+event.values[0]);
        tv.setText("You have taken \n"+String.valueOf((int)mSteps)+" \nSteps");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Log.i(TAG,"onAccuracyChanged");
    }

    protected void onPause() {
        // if unregister this hardware will not detected the step changes
        // mSensorManager.unregisterListener(this);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }
}