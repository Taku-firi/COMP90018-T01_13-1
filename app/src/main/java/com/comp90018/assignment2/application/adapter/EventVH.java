package com.comp90018.assignment2.application.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp90018.assignment2.R;
import com.google.android.material.textview.MaterialTextView;

public class EventVH extends RecyclerView.ViewHolder {
    public MaterialTextView tvName,tvDate,tvType;
    public View card;

    public EventVH(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.text_card_name);
        tvDate = itemView.findViewById(R.id.text_card_date);
        tvType = itemView.findViewById(R.id.text_card_type);
        card = itemView.findViewById(R.id.layout_event);

    }
}
