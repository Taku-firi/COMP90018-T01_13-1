package com.comp90018.assignment2.application.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Dialog;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.comp90018.assignment2.R;
import com.google.android.material.button.MaterialButton;

public class EventDialog extends DialogFragment {
    public static final String K_TITLE = "k_title";

    private String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle in = getArguments();
        if (in!=null){
            title= in.getString(K_TITLE);
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
        titleTv.setText(title);

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
                Toast.makeText(getActivity(),"Event joined: "+title,Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
