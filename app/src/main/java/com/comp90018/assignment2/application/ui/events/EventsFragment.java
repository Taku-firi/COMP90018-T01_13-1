package com.comp90018.assignment2.application.ui.events;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.comp90018.assignment2.R;
import com.comp90018.assignment2.application.CreateEventActivity;
import com.comp90018.assignment2.application.objects.Event;
import com.comp90018.assignment2.application.utils.DaoEvent;
import com.comp90018.assignment2.application.utils.EventDialog;
import com.comp90018.assignment2.application.utils.PermissionsChecker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener,GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,

    };

    private EventsViewModel eventsViewModel;
    private GoogleMap map;
    private CameraPosition cameraPosition;

    private Location lastKnownLocation;

//    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private final LatLng defaultLocation = new LatLng(-37.7963,144.9614);
    private static final int DEFAULT_ZOOM = 16;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    FloatingActionButton fab;

    DaoEvent daoEvent;

    ArrayList<Event> eventslist = new ArrayList<>();
//    private Double longitude,latitude;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        if (savedInstanceState != null){
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        eventsViewModel =
                new ViewModelProvider(this).get(EventsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_events, container, false);


        doCheckPermission();
        daoEvent = new DaoEvent();
        // Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this.getActivity());

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab=root.findViewById(R.id.floating_action_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putDouble("Latitude",lastKnownLocation.getLatitude());
                bundle.putDouble("Longitude",lastKnownLocation.getLongitude());

                Intent intent = new Intent(getActivity(),CreateEventActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap){
        this.map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        map.setOnMarkerClickListener(this);
        enableMyLocation();
        getCurrentLocation();

        addMarkers();
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation(){
        if (doCheckPermission()) {
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG)
//                .show();
    }


    @Override
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT)
//                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    private void addMarkers(){
        daoEvent.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventData : snapshot.getChildren()){
                    Event event = eventData.getValue(Event.class);
                    eventslist.add(event);
                    LatLng loc=new LatLng(event.getLatitude(),event.getLongitude());
                    if(event.getType().equals("Offline")){
                        map.addMarker(new MarkerOptions().position(loc).title(event.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }else {
                        map.addMarker(new MarkerOptions().position(loc).title(event.getName()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        LatLng location1 = new LatLng(-37.7963,144.9614);
//        map.addMarker(new MarkerOptions().position(location1).title("Activity 1"));
//        LatLng location2 = new LatLng(-37.7965,144.9622);
//        Marker mak2 = map.addMarker(new MarkerOptions().position(location2).title("Activity 2"));
//        mak2.hideInfoWindow();
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String title = marker.getTitle();
        String date ="";
        String detail = "";
        String type = "";

        for (int i=0;i<eventslist.size();i++){
            Event cEvent = eventslist.get(i);
            if (cEvent.getName().equals(title)){
                date = cEvent.getDate();
                detail = cEvent.getDetail();
                type = cEvent.getType();
//                Log.d("EVENT CLICK",date+" "+detail);
            }
        }
//        Toast.makeText(getActivity(),"Activity:"+title,Toast.LENGTH_SHORT).show();
        popEventDialog(title,date,detail,type);
        return false;
    }

    private void popEventDialog(String title, String date, String detail, String type){
        EventDialog dialog = new EventDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EventDialog.K_TITLE,title);
        bundle.putString(EventDialog.K_DATE,date);
        bundle.putString(EventDialog.K_DETAIL,detail);
        bundle.putString(EventDialog.K_TYPE,type);
        dialog.setArguments(bundle);
        dialog.show(getActivity().getSupportFragmentManager(),"TAG");
    }

    public boolean doCheckPermission(){
        PermissionsChecker mPermissionsChecker = new PermissionsChecker(this.getActivity());
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(this.getActivity(),PERMISSIONS,1);
            return false;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        Task<Location> task = client.getLastLocation();
        task.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()){
                    lastKnownLocation = task.getResult();
                    if (lastKnownLocation!=null){
                        LatLng latLng = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                               latLng,DEFAULT_ZOOM));
//                        map.addMarker(new MarkerOptions().position(latLng).title("I'm here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }else {
                        Log.d("MAP","Current location is null. Using defaults.");
                        Log.e("MAP", "Exception: %s", task.getException());
                        map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                    }
                }
            }
        });

    }


}
