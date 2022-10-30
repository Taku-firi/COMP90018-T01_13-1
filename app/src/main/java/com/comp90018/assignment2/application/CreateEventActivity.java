package com.comp90018.assignment2.application;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.comp90018.assignment2.R;
import com.comp90018.assignment2.application.objects.User;
import com.comp90018.assignment2.application.objects.Event;
import com.comp90018.assignment2.application.utils.DaoEvent;
import com.comp90018.assignment2.application.utils.DaoUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    private TextInputEditText eventDate;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");

        Double Lat = bundle.getDouble("Latitude");
        Double Long = bundle.getDouble("Longitude");

        TextInputEditText eventName = findViewById(R.id.event_name);
        AppCompatSpinner spinner = findViewById(R.id.event_category);
        TextInputEditText eventDetail = findViewById(R.id.event_detail);

        DaoEvent daoEvent= new DaoEvent();
        DaoUser daoUser = new DaoUser();

        eventDate = findViewById(R.id.event_date);
        eventDate.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });

        eventDate.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDatePickDlg();
                }
            }
        });

        MaterialButton launchBtn = findViewById(R.id.launch_btn);
        launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eventName.getText().toString();
                String detail = eventDetail.getText().toString();
                String date = eventDate.getText().toString();
                String category=spinner.getSelectedItem().toString();

                Event cEvent = new Event(name,Lat,Long);
                cEvent.setDate(date);
                cEvent.setDetail(detail);
                cEvent.setType(category);

                daoEvent.add(cEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateEventActivity.this,"Record is inserted",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateEventActivity.this,"Error inserting"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                SharedPreferences sharedPreferences = getSharedPreferences("assignment2",MODE_PRIVATE);
                String username = sharedPreferences.getString("currentUser","");

                daoUser.getDatabaseReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(username)){
                            User cUser = snapshot.child(username).getValue(User.class);
                            ArrayList<Event> myevents = cUser.getEvents();
                            myevents.add(cEvent);
                            daoUser.add(cUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Toast.makeText(CreateEventActivity.this,"launched: "+ name+date+category+detail,Toast.LENGTH_LONG).show();
                Log.d("EVENT OBJECT",cEvent.getDate()+" "+cEvent.getDetail()+" "+cEvent.getName()+" "+cEvent.getType()+""+cEvent.getLatitude()+" "+cEvent.getLongitude());
                finish();
            }
        });



    }


    protected void showDatePickDlg(){
        Calendar calendar = Calendar.getInstance();

        final int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        final int mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (year < mYear) {
                    view.updateDate(mYear, mMonth, mDayOfMonth);
                    //        Toast.makeText(getActivity(),"Activity:"+title,Toast.LENGTH_SHORT).show();
                    Toast.makeText(CreateEventActivity.this,"Past year",Toast.LENGTH_SHORT).show();

                    return;
                }

                if (year == mYear && month < mMonth) {
                    view.updateDate(mYear, mMonth, mDayOfMonth);
                    Toast.makeText(CreateEventActivity.this,"Past month",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (year == mYear && month == mMonth && dayOfMonth < mDayOfMonth) {
                    view.updateDate(mYear, mMonth, mDayOfMonth);
                    Toast.makeText(CreateEventActivity.this,"Past day",Toast.LENGTH_SHORT).show();
                    return;
                }

                CreateEventActivity.this.eventDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);

            }
        }, mYear,mMonth,mDayOfMonth);
        datePickerDialog.show();
    }

}
