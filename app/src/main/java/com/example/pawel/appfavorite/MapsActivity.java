package com.example.pawel.appfavorite;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double longitude;
    double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            currentlocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);



        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("places");
        ValueEventListener postListener  = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Place place = new Place();
                    place.setCity(data.getValue(Place.class).getCity());
                    place.setLatitude(data.getValue(Place.class).getLatitude());
                    place.setLongitude(data.getValue(Place.class).getLongitude());
                    place.setName(data.getValue(Place.class).getName());
                    mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLatitude(),place.getLongitude())).title(place.getName()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(postListener);
    }
    public void currentlocation() throws Exception {
        LocationManager mylm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria cr = new Criteria();
        String mojDostawca = mylm.getBestProvider(cr, true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET
                },10);
            }
            return;
        }
        Location loc = mylm.getLastKnownLocation(mojDostawca);
        if(loc ==null)
        {
            loc = mylm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        longitude = loc.getLongitude();
        latitude = loc.getLatitude();

    }

}
