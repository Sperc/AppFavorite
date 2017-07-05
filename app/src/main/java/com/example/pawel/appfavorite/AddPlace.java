package com.example.pawel.appfavorite;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.BuddhistCalendar;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class AddPlace extends AppCompatActivity {

    private Button addButton;
    private ImageButton backButton;
    private EditText editCity, editName;
    private TextView longtitude, latitude;
    private double glong;
    private double glati;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        initialize();
        try {
            currentlocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onClick();
        //changedLocation();



    }

    private void changeDisplayedCoordinates(Location loc) {
        Coordinates c = new Coordinates();
        glati = loc.getLatitude();
        glong = loc.getLongitude();

        longtitude.setText("Dlugosc:   " + c.changeDecimalToDeegre(glong));
        latitude.setText("Szerokosc: " + c.changeDecimalToDeegre(glati));
    }


    public void initialize() {
        addButton = (Button) findViewById(R.id.addButton);
        backButton = (ImageButton)findViewById(R.id.backBtn);
        longtitude = (TextView) findViewById(R.id.textView2);
        latitude = (TextView) findViewById(R.id.textView);
        editCity = (EditText) findViewById(R.id.editMiasto);
        editName = (EditText) findViewById(R.id.editNazwa);
    }

    public void currentlocation() throws Exception {
        LocationManager mylm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria cr = new Criteria();
        String mojDostawca = mylm.getBestProvider(cr, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET
                },10);
            }
            return;
    }
        Location loc = mylm.getLastKnownLocation(mojDostawca);
        if(loc ==null)
        {
            loc = mylm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        changeDisplayedCoordinates(loc);

    }
//    public void changedLocation() {
//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locationListener = new MyLocationListener();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET
//                },10);
//            }
//            return;
//        }
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
//    }
//
//    private class MyLocationListener implements LocationListener {
//
//        @Override
//        public void onLocationChanged(Location loc) {
//            Toast.makeText(
//                    getBaseContext(),
//                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//            changeDisplayedCoordinates(loc);
//
//        /*------- To get city name from coordinates -------- */
////            String cityName = null;
////            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
////            List<Address> addresses;
////            try {
////                addresses = gcd.getFromLocation(loc.getLatitude(),
////                        loc.getLongitude(), 1);
////                if (addresses.size() > 0) {
////                    System.out.println(addresses.get(0).getLocality());
////                    cityName = addresses.get(0).getLocality();
////                }
////            }
////            catch (IOException e) {
////                e.printStackTrace();
////            }
////            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
////                    + cityName;
////            editLocation.setText(s);
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {}
//
//        @Override
//        public void onProviderEnabled(String provider) {}
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//    }

    public void onClick()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              createAlertDialogWithButtons();
            }
        });
    }
    private void addPlace()
    {
        Place p = new Place();
        p.setLatitude(glati);
        p.setLongitude(glong);
        p.setCity(editCity.getText().toString());
        p.setName(editName.getText().toString());

        //dodawanie do bazy danych
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if(!p.getCity().equals("") && !p.getName().equals(""))
        {
            databaseReference.child("places").child(p.getName()+"-"+p.getCity()).setValue(p);
            Toast.makeText(getApplicationContext(),"Zapisano miejsce",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Uzupelnij wszystkie pola",Toast.LENGTH_SHORT).show();
        }
    }
    private void createAlertDialogWithButtons() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Dodaj");
        dialogBuilder.setMessage("Czy napewno chcesz dodac miejsce?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Tak", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                addPlace();
            }
        });
        dialogBuilder.setNegativeButton("Nie", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.show();
    }


}
