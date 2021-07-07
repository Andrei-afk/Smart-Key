package com.example.aplicatie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddCar extends Activity {

    Person p;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addc_layout);

        Intent activityThatCalled = getIntent();

        p = (Person) activityThatCalled.getSerializableExtra("User");



    }



    public void add_new_car(View view) throws JSONException {



        TextView nrmat = (TextView) findViewById(R.id.mat);
        TextView err = (TextView)findViewById(R.id.error);
        TextView br = (TextView)findViewById(R.id.brand);
        TextView md = (TextView)findViewById(R.id.model);
        TextView yy= (TextView)findViewById(R.id.year);
        TextView oil = (TextView) findViewById(R.id.lastOil);
        TextView pads = (TextView) findViewById(R.id.lasBraking);
        TextView tw = (TextView)findViewById(R.id.idBM);

        String idBM = tw.getText().toString();

        String licPlate = nrmat.getText().toString();
        String brand = br.getText().toString();
        String model = md.getText().toString();
        
        String email = p.getEmail();

        if (licPlate.isEmpty() || brand.isEmpty() || model.isEmpty() || yy.getText().toString().isEmpty() || oil.getText().toString().isEmpty() || pads.getText().toString().isEmpty() || tw.getText().toString().isEmpty())
        {
            err.setText(R.string.registerErrorMsg1);
        }
        else
        {
            int year = Integer.parseInt(yy.getText().toString());
            int oilKm = Integer.parseInt(oil.getText().toString());
            int padsKm = Integer.parseInt(pads.getText().toString());
            Car newCar = new Car (brand,model,year,licPlate,email, oilKm, padsKm, idBM) ;


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Cars");

            String id = myRef.push().getKey();
            myRef.child(id).setValue(newCar);

        }









    }










}
