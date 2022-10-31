package com.comp90018.assignment2.application.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Dialog;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.comp90018.assignment2.R;
import com.comp90018.assignment2.application.CreateEventActivity;
import com.comp90018.assignment2.application.objects.Event;
import com.comp90018.assignment2.application.objects.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventDialog extends DialogFragment {
    public static final String K_TITLE = "k_title";
    public static final String K_DATE = "k_date";
    public static final String K_DETAIL = "k_detail";
    public static final String K_TYPE = "K_type";
    public static final String K_LAT = "K_lat";
    public static final String K_LONG = "K_long";


    private String title,date,detail,type;
    private Double latitude,longtitude;
    private Event event;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle in = getArguments();
        if (in!=null){
            title= in.getString(K_TITLE);
            date = in.getString(K_DATE);
            detail = in.getString(K_DETAIL);
            type = in.getString(K_TYPE);
            latitude = in.getDouble(K_LAT);
            longtitude = in.getDouble(K_LONG);
            event = new Event(title,latitude,longtitude);
            event.setDate(date);
            event.setDetail(detail);
            event.setType(type);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.dialog_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        TextView titleTv = view.findViewById(R.id.dialog_title);
        TextView dateTv = view.findViewById(R.id.dialog_date);
        TextView detailTv = view.findViewById(R.id.dialog_detail);
        titleTv.setText(title);
        dateTv.setText(date);
        detailTv.setText(detail);

        ImageView typeIv = view.findViewById(R.id.dialog_type);
        if (type.equals("Offline")){
            typeIv.setBackgroundResource(R.drawable.bg_offline);
        }

        MaterialButton btnClose = view.findViewById(R.id.dialog_btn_close);
        MaterialButton btnJoin = view.findViewById(R.id.dialog_btn_join);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DaoUser daoUser = new DaoUser();

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("assignment2",MODE_PRIVATE);
                String username = sharedPreferences.getString("currentUser","");

                daoUser.getDatabaseReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(username)){
                            User cUser = snapshot.child(username).getValue(User.class);
                            ArrayList<Event> myevents = cUser.getEvents();

                            boolean exist =false;

                            for (int i=0;i<myevents.size();i++){
                                Event cEvent =myevents.get(i);
                                if (cEvent.getName().equals(title)){
                                    exist = true;
                                }
                            }

                            if (!exist){
                                myevents.add(event);
                            }
                            daoUser.add(cUser);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dismiss();
            }
        });
    }
}
