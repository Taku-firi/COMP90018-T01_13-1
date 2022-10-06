package com.comp90018.assignment2.application.ui.events;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//implements OnMapReadyCallback
public class EventsFragment extends Fragment implements OnMapReadyCallback {

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
    private static final int DEFAULT_ZOOM = 15;

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

    @Override
    public void onMapReady(GoogleMap googleMap){
        this.map = googleMap;
        getCurrentLocation();

        LatLng location = new LatLng(-37.7963,144.9614);
        map.addMarker(new MarkerOptions().position(location).title("Activity 1"));

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
                        map.addMarker(new MarkerOptions().position(latLng).title("I'm here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
//    @SuppressLint("MissingPermission")
//    private void getLocation(){
//        LocationManager locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        String provider = LocationManager.GPS_PROVIDER;
//
//
//        Location location = locationManager.getLastKnownLocation(provider);
//        Log.i("Locator","Location:"+location);
//        if (location!=null){
//            latitude=location.getLatitude();
//            longitude=location.getLongitude();
//        }else {
//            Log.i("Locator","Location: null");
//        }
//        locationManager.requestLocationUpdates(provider,2000,10,locationListener);
//
//    }
//
//    private final LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(@NonNull Location location) {
//            latitude=location.getLatitude();
//            longitude=location.getLongitude();
//
//        }
//    };
}
