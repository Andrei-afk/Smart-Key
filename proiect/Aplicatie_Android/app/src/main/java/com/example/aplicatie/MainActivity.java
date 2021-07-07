package com.example.aplicatie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {





    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }


        Button login_btn = (Button)findViewById(R.id.loginbtn);
//---------------------------------------------------------------------------------------------verif pt sezor daca se poate folosi
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate())
        {
            case BiometricManager.BIOMETRIC_SUCCESS:
            //    msg_text.setText(R.string.canAuth);

                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
               // msg_text.setText(R.string.errAuth1);
                login_btn.setVisibility(View.INVISIBLE);
                 break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
              //  msg_text.setText(R.string.errAuth2);
                login_btn.setVisibility(View.INVISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
               // msg_text.setText(R.string.errAuth3);
                login_btn.setVisibility(View.INVISIBLE);
                break;
        }


        //-----------------------------------------------------------------------------------------------------------------------------------------------amprenta la login
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Users");
                String id_dev = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ds : snapshot.getChildren())
                        {
                            String idDB = ds.child("id").getValue().toString();
                            if (id_dev.equals(idDB))
                            {
                                String email = ds.child("email").getValue().toString();
                                String firstname = ds.child("firstname").getValue().toString();
                                String lastname = ds.child("lastname").getValue().toString();
                                String pasword = ds.child("password").getKey().toString();

                                Person p = new Person(firstname,lastname,email,pasword,id_dev);

                                Toast toast = Toast.makeText(getApplicationContext(), "Login Succes", Toast.LENGTH_SHORT);
                                toast.show();

                                logSucces(p);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Use your fingerprint to login")
                .setNegativeButtonText("Cancel")
                .build();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });





    }

    public void logSucces(Person p)
    {
        final int rez =1;
        Intent moveToMenu = new Intent(this, MenuScreen.class);
        moveToMenu.putExtra("User",p);

        startActivity(moveToMenu);
    }


    public void register_fnc(View view) {

        Intent moveToRegister = new Intent(this, RegisterScreen.class);

        startActivity(moveToRegister);
    }

    public void loginacc(View view) {


        EditText em = (EditText)findViewById(R.id.emailadress);
        EditText pa = (EditText)findViewById(R.id.password);
        TextView tw = (TextView)findViewById(R.id.msg);

        String email = em.getText().toString();
        String pass = pa.getText().toString();
        if(!pass.equals(""))
        {
            if(!email.equals(""))
            {


                if(email.isEmpty() || pass.isEmpty())
                {
                    tw.setText(R.string.registerErrorMsg1);
                }
                else
                {
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    {
                        tw.setText(R.string.registerErrorMsg3);
                    }
                    else
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference().child("Users");


                        Person p = new Person();

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for(DataSnapshot ds : snapshot.getChildren())
                                {
                                    String em2= ds.child("email").getValue().toString();


                                    if(em2.equals(email)) {

                                        String pass2 = ds.child("password").getValue().toString();
                                        // tw.setText(em2 + " "+ pass);

                                        if (pass2.equals(pass)) {

                                            String fn = ds.child("firstname").getValue().toString();
                                            String ln = ds.child("lastname").getValue().toString();
                                            String id_dev = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                                            p.setEmail(em2);
                                            p.setPassword(pass2);
                                            p.setFirstname(fn);
                                            p.setLastname(ln);
                                            p.setId(id_dev);
                                            final int result = 1;

                                            Intent asd = new Intent(getApplicationContext(), MenuScreen.class);

                                            asd.putExtra("User",p);

                                            startActivityForResult(asd, result);

                                            Toast toast = Toast.makeText(getApplicationContext(), "Login Succes", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        else
                                        {
                                            tw.setText(R.string.loginError);
                                            pa.setText("");
                                            em.setText("");
                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                tw.setText(R.string.dbError);
                            }
                        })   ;


                    }
                }
            }
        }







    }
}