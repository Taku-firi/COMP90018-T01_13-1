package com.comp90018.assignment2.application.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp90018.assignment2.R;
import com.comp90018.assignment2.application.EventDetailActivity;
import com.comp90018.assignment2.application.objects.Event;

import java.util.ArrayList;


// Adapter for items of the schedule page
public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    ArrayList<Event> list= new ArrayList<>();

    public ScheduleAdapter(Context ctx){
        this.context = ctx;
    }

    public void setItems(ArrayList<Event> events){
        list.addAll(events);
    }


    // Set the corresponding view holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_event,parent,false);
        return new EventVH(view);
    }

    // Map corresponding data to UI
    // All the events the user joins are listed
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EventVH vh = (EventVH)holder;
        Event event = list.get(position);
        vh.tvName.setText(event.getName());
        vh.tvDate.setText(event.getDate());
        vh.tvType.setText(event.getType());
        if (event.getType().equals("Offline")){
            vh.image.setImageResource(R.drawable.bg_offline);
        }
        // While user click on a certain event, start the event detail activity
        vh.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EventDetailActivity.class));
                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
