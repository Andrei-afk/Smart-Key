package com.example.aplicatie;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.common.data.DataBufferRef;
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
import java.nio.channels.Channel;
import java.util.UUID;

public class CarScreen extends Activity {


    Button btn1,btn2,btn3;
    String address = null;

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    private android.os.Handler mainHandler = new Handler();
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    String adr;
    static String EXTRA_ADDRES="device_adres";

    boolean pressedOil = false;
    boolean pressedPads = false;
    boolean pressedErr = false;
    boolean pressedLock = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_screen_layout);

        Intent intent = getIntent();
        address = intent.getStringExtra(MenuScreen.EXTRA_ADDRESS);



        btn1 =  findViewById(R.id.button1);
       // btn2 =  findViewById(R.id.button2);
        btn3  = findViewById(R.id.button3);

        new ConnectBT().execute();



       /* btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSignal("0");
                msg("car locked");
            }
        });
*/

    }




    //trimite semnalu la arduino
    private void sendSignal ( String number ) {
        if ( btSocket != null ) {
            try {
                btSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }


    private void msg (String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    //la disconect
    private void Disconnect () {
        if ( btSocket!=null ) {
            try {
                btSocket.close();
            } catch(IOException e) {
                msg("Error");
            }
        }

        finish();
    }

    public void lockUnlock(View view) {
        if(pressedLock == false)
        {
            sendSignal("1");
            msg("Car unlocked");
            btn1.setBackground(getDrawable(R.drawable.unlock_button));
            pressedLock = true;
        }
        else
        {
            sendSignal("0");
            msg("Car locked");
            btn1.setBackground(getDrawable(R.drawable.lock_button));
            pressedLock = false;
        }

    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(CarScreen.this, "Connecting...", "Please Wait!!!");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if ( btSocket==null || !isBtConnected ) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();

                    adr = btSocket.getRemoteDevice().getAddress();

                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected");
                isBtConnected = true;
            }

            progress.dismiss();
        }
    }






    public void show(View view) {

        Intent intent = new Intent(this,CarInfo.class);
        intent.putExtra(EXTRA_ADDRES, adr);
        startActivity(intent);
    }





    public void showErrHistory(View view) {

        Intent intent = new Intent(this,CarErrorScreen.class);
        intent.putExtra(EXTRA_ADDRES, adr);
        startActivity(intent);

    }




}
