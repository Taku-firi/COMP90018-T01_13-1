package com.comp90018.assignment2.application.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp90018.assignment2.R;
import com.google.android.material.textview.MaterialTextView;


// ViewHolder of each event
// Dealing with metadata
public class EventVH extends RecyclerView.ViewHolder {
    public MaterialTextView tvName,tvDate,tvType;
    public View card;
    public ImageView image;

    public EventVH(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.text_card_name);
        tvDate = itemView.findViewById(R.id.text_card_date);
        tvType = itemView.findViewById(R.id.text_card_type);
        image = itemView.findViewById(R.id.img_card_event);
        card = itemView.findViewById(R.id.layout_event);

    }
}
