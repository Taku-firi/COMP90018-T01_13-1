package com.comp90018.assignment2.application;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.comp90018.assignment2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

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
                Toast.makeText(CreateEventActivity.this,"launched",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
//        MaterialTextView tv1 = findViewById(R.id.tv1);
//        MaterialTextView tv2 = findViewById(R.id.tv2);
//        tv1.setText(Lat.toString());
//        tv2.setText(Long.toString());

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
