package com.comp90018.assignment2.application.ui.events;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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

import com.comp90018.assignment2.R;
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

        // Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this.getActivity());

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab=root.findViewById(R.id.floating_action_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_SHORT).show();
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
            return;
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
        LatLng location = new LatLng(-37.7963,144.9614);
        map.addMarker(new MarkerOptions().position(location).title("Activity 1"));
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String title = marker.getTitle();
        Toast.makeText(getActivity(),"Activity:"+title,Toast.LENGTH_SHORT).show();

        return false;
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
