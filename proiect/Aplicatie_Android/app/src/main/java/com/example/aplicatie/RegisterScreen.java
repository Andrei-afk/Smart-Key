package com.example.aplicatie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static java.security.AccessController.getContext;

public class RegisterScreen extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_layout);

    }


    public void register_new_person(View view) {

        TextView tw = (TextView) findViewById(R.id.error);

        EditText fn = (EditText)findViewById(R.id.firstname);
        EditText ln = (EditText)findViewById(R.id.lastname);
        EditText em = (EditText)findViewById(R.id.emailadress);
        EditText p = (EditText)findViewById(R.id.pass);
        EditText pr = (EditText)findViewById(R.id.passRep);

        String firstname = fn.getText().toString();
        String lastname = ln.getText().toString();
        String email = em.getText().toString();
        String password = p.getText().toString();
        String repassword = pr.getText().toString();

        final boolean[] checkEm = {false};

        if(firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || repassword.isEmpty())
        {
            tw.setText(R.string.registerErrorMsg1);
        }
        else
        {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                tw.setText(R.string.registerErrorMsg2);
            }
            else
            {
                if(!(password.equals(repassword)) )
                {
                    tw.setText(R.string.registerErrorMsg3);
                }
                else
                {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Users");

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren())
                            {
                                String emdb= ds.child("email").getValue().toString();

                                if(emdb.equals(email) && !checkEm[0])
                                {
                                    checkEm[0] = true;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            tw.setText(R.string.dbError);
                        }
                    });

                    if(!checkEm[0])
                    {
                        String id_dev = Settings.Secure.getString(getContentResolver(),
                                Settings.Secure.ANDROID_ID);

                        Person newUser = new Person(firstname,lastname,email,password, id_dev);

                        database = FirebaseDatabase.getInstance();

                        myRef = database.getReference().child("Users");

                        String id = myRef.push().getKey();
                        myRef.child(id).setValue(newUser);

                        Toast toast = Toast.makeText(getApplicationContext(), "Register Succes", Toast.LENGTH_SHORT);
                        toast.show();

                        logSucces(newUser);
                    }
                    else
                    {
                        tw.setText(R.string.registerErrorMsg4);
                    }

                }
            }
        }
    }

    public void logSucces(Person p)
    {
        final int rez =1;
        Intent moveToMenu = new Intent(this, MenuScreen.class);
        moveToMenu.putExtra("User",p);

        startActivity(moveToMenu);
    }




}
