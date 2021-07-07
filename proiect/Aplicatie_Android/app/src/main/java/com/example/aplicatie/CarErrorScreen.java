package com.example.aplicatie;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarErrorScreen extends Activity {
    boolean pressedOil = false;
    boolean pressedPads = false;
    boolean pressedErr = false;
    String adr;
    Spinner mySpinner;
    ArrayAdapter<String> myAdapter;

    // String[] spinnerValueHoldValue = {"PHP", "ANDROID", "WEB-DESIGN", "PHOTOSHOP"};
    ArrayList<String> spinnerValueHoldValue = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_error_screen);
        Intent intent = getIntent();
        adr = intent.getStringExtra(CarScreen.EXTRA_ADDRES);
        pressedErr = false;


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Cars");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String lp = "";
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String adrr = ds.child("idBle").getValue().toString();

                    if (adrr.equals(adr)) {
                        lp = ds.child("carLP").getValue().toString();
                    }
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = database.getReference().child("Errors").child(lp);
                ArrayList<String> setErrors = new ArrayList<String>();
                setErrors.add("All");
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String errorName = ds.child("errorName").getValue().toString();
                            if (!setErrors.contains(errorName))
                                setErrors.add(errorName);
                        }
                        mySpinner = (Spinner) findViewById(R.id.spinner1);
                        myAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, setErrors);
                        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mySpinner.setAdapter(myAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }





    public void showErr(String filtru) {

        pressedErr = true;
       // EditText ed = (EditText) findViewById(R.id.errorHistory);
       // ed.setText("");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Cars");

        myRef.addValueEventListener(new ValueEventListener() {
            List<ErrorCar> errorListCard=new ArrayList<ErrorCar>();
            
            String lp = "";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String adrr = dataSnapshot.child("idBle").getValue().toString();

                    if (adrr.equals(adr)) {
                        lp = dataSnapshot.child("carLP").getValue().toString();
                    }
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = database.getReference().child("Errors").child(lp);

                myRef2.addValueEventListener(new ValueEventListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (filtru.equals("All")) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String carBrand = dataSnapshot.child("carBrand").getValue().toString();
                                String carModel = dataSnapshot.child("carModel").getValue().toString();
                                String todayDate = dataSnapshot.child("todayDate").getValue().toString();
                                String errorName = dataSnapshot.child("errorName").getValue().toString();
                                String errorMsg = dataSnapshot.child("errorText").getValue().toString();

                              //  ed.setText(ed.getText() + carBrand + "--" + carModel + "--" + todayDate + "--" + errorName + "--" + errorMsg + "\n");

                                String email = dataSnapshot.child("email").getValue().toString();
                                ErrorCar err = new ErrorCar(carModel,carBrand,errorName,errorMsg,email,todayDate);

                                errorListCard.add(err);
                            }
                        } else {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String errorName = dataSnapshot.child("errorName").getValue().toString();
                                if (errorName.equals(filtru)) {
                                    String carBrand = dataSnapshot.child("carBrand").getValue().toString();
                                    String carModel = dataSnapshot.child("carModel").getValue().toString();
                                    String todayDate = dataSnapshot.child("todayDate").getValue().toString();
                                    String errorMsg = dataSnapshot.child("errorText").getValue().toString();
                                 //   ed.setText(ed.getText() + carBrand + "--" + carModel + "--" + todayDate + "--" + errorName + "--" + errorMsg + "\n");
                                    String email = dataSnapshot.child("email").getValue().toString();
                                    ErrorCar err = new ErrorCar(carBrand, carModel, errorName,errorMsg,email,todayDate);
                                    errorListCard.add(err);
                                }
                            }

                        }

                      //  Window w = getWindow();
                       // w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                        RecyclerView recyclerView = findViewById(R.id.recyvleView);
                        Adapter adapter = new Adapter(getApplicationContext(),errorListCard);

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void changeOil(View view) {
        EditText et = (EditText)findViewById(R.id.KmOilNew);
        String s = et.getText().toString();
        if(s.equals(""))
            Toast.makeText(getApplicationContext(),"Insert a value", Toast.LENGTH_SHORT);
        else
        {
            if( !pressedOil )
            {
                pressedOil = true;

                int newKmOil = Integer.parseInt(et.getText().toString());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Cars");

                myRef.addValueEventListener(new ValueEventListener() {
                    String key;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            String adrr = dataSnapshot.child("idBle").getValue().toString();

                            if(adrr.equals(adr))
                            {
                                key=dataSnapshot.getKey();
                            }
                        }
                        myRef.child(key).child("oilKm").setValue(newKmOil);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(),"You have already done it!", Toast.LENGTH_SHORT);
            }
        }

    }

    public void changePads(View view) {
        EditText et = (EditText)findViewById(R.id.KmPadsNew);
        String s = et.getText().toString();
        if(s.equals(""))
            Toast.makeText(getApplicationContext(),"Insert a value", Toast.LENGTH_SHORT);
        if( !pressedPads )
        {
            pressedPads = true;

            int newKmPads = Integer.parseInt(et.getText().toString());

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Cars");

            myRef.addValueEventListener(new ValueEventListener() {
                String key;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        String adrr = dataSnapshot.child("idBle").getValue().toString();

                        if(adrr.equals(adr))
                        {
                            key=dataSnapshot.getKey();
                        }
                    }
                    myRef.child(key).child("padsKm").setValue(newKmPads);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"You have already done it!", Toast.LENGTH_SHORT);
        }
    }

    public void ads(View view) {
        //Toast.makeText(this, mySpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
        showErr(mySpinner.getSelectedItem().toString());
    }
}
