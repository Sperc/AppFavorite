package com.example.pawel.appfavorite;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.BooleanResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Details extends AppCompatActivity {

    private Place place;
    private TextView nameView;
    private EditText nameEdit,latitEdit,longiEdit,cityEdit;
    private Toolbar toolbar;
    private ImageButton backBtn,editBtn,deleteBtn;
    private Button saveBtn;
    private DatabaseReference databaseReference;
    private Boolean editFlag;
    int counter_to_exit_on_edit_mode;
    private String valuesFromView[]; // kolejnosc: name,city,lati,longi !!!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        Intent intent = getIntent();
        place = (Place) intent.getSerializableExtra("place");
        initialize();
        setEditTextEnabled(false);
        onClick();
        editFlag = false;

    }

    public void initialize()
    {
        nameView = (TextView)findViewById(R.id.textName);
        nameEdit = (EditText)findViewById(R.id.editText);
        cityEdit = (EditText)findViewById(R.id.editText2);
        latitEdit = (EditText)findViewById(R.id.editText3);
        longiEdit = (EditText)findViewById(R.id.editText4);
        toolbar = (Toolbar)findViewById(R.id.toolbar_details);
        deleteBtn = (ImageButton)findViewById(R.id.deleteBtn);
        backBtn = (ImageButton)findViewById(R.id.backBtn);
        editBtn = (ImageButton)findViewById(R.id.editButton);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valuesFromView = new String[4];


        Coordinates coordinates = new Coordinates();
        nameView.setText(place.toString());
        nameEdit.setText(place.getName());
        cityEdit.setText(place.getCity());
        latitEdit.setText(coordinates.changeDecimalToDeegre(place.getLatitude()));
        longiEdit.setText(coordinates.changeDecimalToDeegre(place.getLongitude()));
        latitEdit.setEnabled(false);
        longiEdit.setEnabled(false);


    }

    public void onClick()
    {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editFlag==false)
                {
                    Intent intent = new Intent(getApplicationContext(), ListOfPlaces.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    editFlag=false;
                    setValuesOfView();
                    setEditTextEnabled(false);
                    saveBtn.setVisibility(View.INVISIBLE);
                }

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseReference.child("places").child(place.toString()).removeValue();
//                Toast.makeText(getApplicationContext(),"Usunieto",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(),ListOfPlaces.class)
//                startActivity(intent);
//                finish();
                createAlertDialogWithButtonsToDelete();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFlag = true;
                setEditTextEnabled(true);
                saveBtn.setVisibility(View.VISIBLE);
                getValuesOfView();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogWithButtonsToEdit();
            }
        });
    }
    private void deletePlace()
    {
        databaseReference.child("places").child(place.toString()).removeValue();
        Toast.makeText(getApplicationContext(),"Usunieto",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),ListOfPlaces.class);
        startActivity(intent);
        finish();
    }
    private void editPlace()
    {
        Coordinates c = new Coordinates();
        Place p = new Place();
        p.setLatitude(c.changeDeegreToDecimal(latitEdit.getText().toString()));
        p.setLongitude(c.changeDeegreToDecimal(longiEdit.getText().toString()));
        p.setCity(cityEdit.getText().toString());
        p.setName(nameEdit.getText().toString());

        //dodawanie do bazy danych
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if(!p.getCity().equals("") && !p.getName().equals(""))
        {
            databaseReference.child("places").child(place.toString()).removeValue();
            databaseReference.child("places").child(p.getName()+"-"+p.getCity()).setValue(p);
            nameView.setText(p.toString());
            getValuesOfView();
            Toast.makeText(getApplicationContext(),"Zmieniono miejsce",Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Uzupelnij wszystkie pola",Toast.LENGTH_SHORT).show();
        }
    }

    private void getValuesOfView()
    {
        valuesFromView[0] = nameEdit.getText().toString();
        valuesFromView[1] = cityEdit.getText().toString();
    }
    private void setValuesOfView()
    {
        nameEdit.setText(valuesFromView[0]);
        cityEdit.setText(valuesFromView[1]);
    }
    public void setEditTextEnabled(boolean flag)
    {
        nameEdit.setEnabled(flag);
        cityEdit.setEnabled(flag);
    }
    private void createAlertDialogWithButtonsToDelete() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Usuwanie");
        dialogBuilder.setMessage("Czy napewno?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Tak", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                deletePlace();
                finish();
            }
        });
        dialogBuilder.setNegativeButton("Nie", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.show();
    }
    private void createAlertDialogWithButtonsToEdit() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Zapisywanie");
        dialogBuilder.setMessage("Czy napewno?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Tak", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                editPlace();

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
