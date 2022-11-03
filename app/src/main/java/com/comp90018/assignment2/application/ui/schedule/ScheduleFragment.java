package com.comp90018.assignment2.application.ui.schedule;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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
import com.comp90018.assignment2.application.objects.User;
import com.comp90018.assignment2.application.utils.DaoEvent;
import com.comp90018.assignment2.application.utils.DaoUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// The fragment of the event map
public class ScheduleFragment extends Fragment {
    private ScheduleViewModel scheduleViewModel;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    DaoEvent daoEvent;
    DaoUser daoUser;
    boolean isLoading = false;
    String key = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scheduleViewModel =
                new ViewModelProvider(this).get(ScheduleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Initialize components
        swipeRefreshLayout = root.findViewById(R.id.swip);
        recyclerView = root.findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        adapter = new ScheduleAdapter(getContext());
        recyclerView.setAdapter(adapter);
        daoEvent = new DaoEvent();
        daoUser = new DaoUser();
        loadEvents();

        return root;
    }

    // Method for load user's events
    private void loadEvents() {
        swipeRefreshLayout.setRefreshing(true);

        // Identify the current user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("assignment2",MODE_PRIVATE);
        String username = sharedPreferences.getString("currentUser","");

        // Require all the events the user joined in
        daoUser.getDatabaseReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username)){
                    User cUser = snapshot.child(username).getValue(User.class);
                    ArrayList<Event> myevents = cUser.getEvents();
                    adapter.setItems(myevents);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
