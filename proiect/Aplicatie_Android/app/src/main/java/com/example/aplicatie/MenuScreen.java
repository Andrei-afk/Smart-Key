package com.example.aplicatie;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class MenuScreen extends Activity {

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_addres";
    ListView devicelist;


    TextView t ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_layout);

//----------------------------------------------------------------------------------------------------------------------------------------imi ia numele de la login
        Intent activityThatCalled = getIntent();

        Person p = (Person) activityThatCalled.getSerializableExtra("User");

        TextView msg = (TextView) findViewById(R.id.tv);

        msg.append("Welcome "+ p.getLastname());

 //------------------------------------------------------------------------------------------------------------------------------

        //---------------------------------------------------------------verificam la bluetooth

        Button btnPaired;

        btnPaired = (Button) findViewById(R.id.button);
        devicelist = (ListView) findViewById(R.id.listView);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if ( myBluetooth==null ) {
            Toast.makeText(getApplicationContext(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
            finish();
        }
        //daca are bluetoothu oprit il porneste cu permisiune
        else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });

    }

    //----------------------------------------------------------------------conectarea propriu zisa
    int nrHc05=0;
    private void pairedDevicesList ()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        //ArrayList list = new ArrayList();

        if ( pairedDevices.size() > 0 ) {
            for ( BluetoothDevice bt : pairedDevices ) {
              if(bt.getName().equals("HC-05"))
              {
                  nrHc05++;
              }
            }

        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }


        ListView lw = findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter();
        lw.setAdapter(customAdapter);
        lw.setOnItemClickListener(myListClickListener);

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView addr = (TextView)view.findViewById(R.id.btAdress);
            String address = addr.getText().toString();

            Intent i = new Intent(MenuScreen.this, CarScreen.class);
            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };

    //-----------------------------------------------------------------------gata conectarea



    public void add(View view) {



        Intent asdd = new Intent(this, AddCar.class);
        final int result = 1;

        Intent activityThatCalled = getIntent();
        Person p = (Person) activityThatCalled.getSerializableExtra("User");

        asdd.putExtra("User",p);

        startActivity(asdd);

        startActivity(asdd);

    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return nrHc05+1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_list_component,null);
            TextView tvName = (TextView)convertView.findViewById(R.id.btName);
            TextView tvAdress = (TextView)convertView.findViewById(R.id.btAdress);

            String name="";String adr="";
            int cureentIndex=0;
            for ( BluetoothDevice bt : pairedDevices ) {
                if(bt.getName().equals("HC-05"))
                {
                    cureentIndex++;
                    if(cureentIndex == position)
                    {
                        name = bt.getName().toString();
                        adr = bt.getAddress().toString();
                    }
                }

            }


            tvName.setText(name);
            tvAdress.setText(adr);
            return convertView;
        }
    }

     //-------------------------------------------------------------------------------pt afisare in timp real




    //------------------------------------------------------------------------------------------------------------------------------
}
