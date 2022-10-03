package com.comp90018.assignment2.application.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.comp90018.assignment2.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class EventsFragment extends Fragment implements OnMapReadyCallback{

    private EventsViewModel eventsViewModel;
    GoogleMap map;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        eventsViewModel =
                new ViewModelProvider(this).get(EventsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_events, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        LatLng location = new LatLng(-37.7963,144.9614);
        map.addMarker(new MarkerOptions().position(location).title("You are here"));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
