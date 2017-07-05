package com.example.pawel.appfavorite;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/**
 * Created by Pawel on 17.06.2017.
 */

public class Place implements Serializable{
    private String city;
    private String name;
    private double latitude;
    private double longitude;
    public Place()
    {

    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() { return city; }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
//        return "\nNazwa: "+getName()+"-"+ getCity()+"\nnazwa miejsca: "+
//                getName()+"\nmiasto: "+
//                getCity()+"\nszerokosc: "+
//                getLatitude()+"\ndlugosc: "+
//                getLongitude()+"\n";
        return getName()+"-"+getCity();
    }
}
