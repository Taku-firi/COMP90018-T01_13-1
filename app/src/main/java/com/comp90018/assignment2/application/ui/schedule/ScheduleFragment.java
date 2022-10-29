package com.comp90018.assignment2.application.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.comp90018.assignment2.R;
import com.comp90018.assignment2.application.adapter.ScheduleAdapter;
import com.comp90018.assignment2.application.objects.Event;
import com.comp90018.assignment2.application.utils.DaoEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment {
    private ScheduleViewModel scheduleViewModel;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    DaoEvent daoEvent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scheduleViewModel =
                new ViewModelProvider(this).get(ScheduleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swip);
        recyclerView = root.findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new ScheduleAdapter(getContext());
        recyclerView.setAdapter(adapter);
        daoEvent = new DaoEvent();
        loadEvents();



        return root;
    }

    private void loadEvents() {
        daoEvent.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Event> eventslist = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()){
                    Event event = data.getValue(Event.class);
                    eventslist.add(event);
                }
                adapter.setItems(eventslist);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
