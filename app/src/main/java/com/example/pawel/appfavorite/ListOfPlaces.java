package com.example.pawel.appfavorite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListOfPlaces extends AppCompatActivity {

    ListView listView;
    ImageButton backBtn;
    DatabaseReference databaseReference;
    ArrayList<Place> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofplaces_activity);
        init();
        onClick();


    }


    public void init()
    {
        backBtn = (ImageButton)findViewById(R.id.backBtn);
        arrayList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.listView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Place p = messageSnapshot.getValue(Place.class);
                    arrayList.add(p);
                }
                listView.setAdapter(new ArrayAdapter<Place>(getBaseContext(),android.R.layout.simple_list_item_activated_1,arrayList));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void onClick()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),arrayList.get(position).toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),Details.class);
                intent.putExtra("place",arrayList.get(position));
                startActivity(intent);
                finish();

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
