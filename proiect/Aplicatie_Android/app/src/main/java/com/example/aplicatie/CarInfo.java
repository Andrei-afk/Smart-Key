package com.example.aplicatie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class CarInfo extends Activity {

    private static final String CHANNEL_ID ="channel_id_notify" ;
    private static  int NOTIFICATION_ID = 1;

    TextView tvinfo,tvbp,tvect,tvfl,tvrpm,tvimp,tvmaf,tvait,tvtp,tvdct,tvta,tver,tvkm, tvkmOil, tvkmBP,tvinfo2;
    private android.os.Handler mainHandler = new Handler();
    String adr = null;
   HashMap<String, String> ob2Errors = new HashMap<String,String>();
   HashMap<String, Integer> lastSecErr = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_info_layout);

        Intent intent = getIntent();
        adr = intent.getStringExtra(CarScreen.EXTRA_ADDRES);

        tvinfo = (TextView)findViewById(R.id.carInfo);
        tvinfo2 = findViewById(R.id.carInfo2);
        tvbp= (TextView)findViewById(R.id.barometric_pressure) ;
        tvect= (TextView)findViewById(R.id.engine_coolant) ;
        tvfl= (TextView)findViewById(R.id.fuel_lvl) ;
        tvrpm= (TextView)findViewById(R.id.engine_rpm) ;
        tvimp= (TextView)findViewById(R.id.intake_pressure) ;
        tvmaf= (TextView)findViewById(R.id.maf) ;
        tvait= (TextView)findViewById(R.id.air_intake) ;
        tvtp= (TextView)findViewById(R.id.throttle) ;
        tvdct= (TextView)findViewById(R.id.dct);
        tvta= (TextView)findViewById(R.id.timming_adv);
        tvkm=(TextView)findViewById(R.id.km);
        tvkmOil = findViewById(R.id.lastoilchange);
        tvkmBP = findViewById(R.id.lastbpchange);

    //initializare erori
        ob2Errors.put("P1192", getString(R.string.p1192));
        ob2Errors.put("P1193", getString(R.string.p1193));
        ob2Errors.put("P1194", getString(R.string.p1194));
        ob2Errors.put("P1195", getString(R.string.p1195));
        ob2Errors.put("P1249", getString(R.string.p1249));
        ob2Errors.put("P1298", getString(R.string.p1298));
        ob2Errors.put("P1009", getString(R.string.p1009));
        ob2Errors.put("P1552", getString(R.string.p1552));
        ob2Errors.put("P1160", getString(R.string.p1160));
        ob2Errors.put("P1396", getString(R.string.p1396));
        ob2Errors.put("P1147", getString(R.string.p1147));
        ob2Errors.put("P1148", getString(R.string.p1148));
        ob2Errors.put ("P1146", getString(R.string.p1146));
        ob2Errors.put ("P1335", getString(R.string.p1335));
        ob2Errors.put( "P1542", getString(R.string.p1542));
        ob2Errors.put("P1342",	getString(R.string.p1342));
        ob2Errors.put("P1411",	getString(R.string.p1411));
        ob2Errors.put("P1551",	getString(R.string.p15551));
        ob2Errors.put("P1700",	getString(R.string.p1700));
        ob2Errors.put("P1912",  getString(R.string.p1912));


        lastSecErr.put("P1192", -3);
        lastSecErr.put("P1193", -3);
        lastSecErr.put("P1194", -3);
        lastSecErr.put("P1195", -3);
        lastSecErr.put("P1249", -3);
        lastSecErr.put("P1298", -3);
        lastSecErr.put("P1009", -3);
        lastSecErr.put("P1552", -3);
        lastSecErr.put("P1160", -3);
        lastSecErr.put("P1396", -3);
        lastSecErr.put("P1147", -3);
        lastSecErr.put("P1148", -3);
        lastSecErr.put("P1146", -3);
        lastSecErr.put("P1335", -3);
        lastSecErr.put("P1542", -3);
        lastSecErr.put("P1342", -3);
        lastSecErr.put("P1411", -3);
        lastSecErr.put("P1551", -3);
        lastSecErr.put("P1700", -3);
        lastSecErr.put("P1912", -3);



        //end erori


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Cars");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    String adrr = ds.child("idBle").getValue().toString();

                     if(adrr.equals(adr))
                    {
                        String nrMat = ds.child("carLP").getValue().toString();
                        String carBrand = ds.child("carBrand").getValue().toString();
                        String carModel = ds.child("carModel").getValue().toString();
                        String email = ds.child("email").getValue().toString();
                        int oilKmBd = Integer.parseInt(ds.child("oilKm").getValue().toString());
                        int padsKmBd = Integer.parseInt(ds.child("padsKm").getValue().toString());
                        String fileName = nrMat.replaceAll("\\s", "");

                        int interval = getOilInterval(ds.child("carModel").getValue().toString());  // intervalu din fisier care ar trb sa fie o baza de date

                        tvinfo.setText(carBrand + " " + carModel);
                        tvinfo2.setText(nrMat);


                        //tvkmm.setText("Last engine oil change at: "+ "\n"+ oilKmBd + "\n" + "Last braking pads change at: "+"\n"+padsKmBd);
                        tvkmOil.setText(ds.child("oilKm").getValue().toString());
                        tvkmBP.setText(ds.child("padsKm").getValue().toString());
                        show(fileName + ".json",oilKmBd,padsKmBd,carBrand,carModel,nrMat,interval,email);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void show(String fileName, int oilKmBd, int padsKmBd, String carBrand , String carModel, String carLp,int interval,String email) {
        ExRunnable runnable = new ExRunnable(fileName, oilKmBd,padsKmBd,carBrand,carModel,carLp,interval,email);
        new Thread(runnable).start();
    }

    class ExRunnable implements Runnable{

        private String name;
        private int oilKm;
        private int padsKm;
        private String carLp;
        private String carBrand;
        private String carModel;
        private int interval;
        private String email;

        public ExRunnable(String name, int oilKm, int padsKm, String carBrand , String carModel, String carLp,int interval, String email)
        {
            this.name= name;
            this.padsKm=padsKm;
            this.oilKm=oilKm;
            this.carBrand=carBrand;
            this.carLp = carLp;
            this.carModel = carModel;
            this.interval = interval;
            this.email = email;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            String json = "";
            try
            {
                InputStream is = getAssets().open(name);

                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                json = new String(buffer,"UTF-8");
                JSONArray jsonArray = new JSONArray(json);


                for(int x=0;x<jsonArray.length();x++)
                {
                    JSONObject obj = jsonArray.getJSONObject(x);
                    String bp = obj.getString("BAROMETRIC_PRESSURE(KPA)");
                    String rpm = obj.getString("ENGINE_RPM");
                    String ect = obj.getString("ENGINE_COOLANT_TEMP");
                    String fl = obj.getString("FUEL_LEVEL");
                    String imp = obj.getString("INTAKE_MANIFOLD_PRESSURE");
                    String maf = obj.getString("MAF");
                    String ait = obj.getString("AIR_INTAKE_TEMP");
                    String tp = obj.getString("THROTTLE_POS");
                    String dtc = obj.getString("DTC_NUMBER");
                    String ta = obj.getString("TIMING_ADVANCE");
                    int kilometers = obj.getInt("KILOMETERS");
                    String km = obj.getString("KILOMETERS");

                    int interval = getOilInterval(carModel);

                    //---alerte pentru schimb de ulei
                    if(x % 20 == 0) //--------------------------------prima notificare
                    {
                        //int remKilo = interval - kilometers - oilKm;
                        int remKilo = interval - ( kilometers - oilKm );

                        if(remKilo >=0)
                            showNotification(String.valueOf(remKilo)+" kilometers left until engine oil change","Change engine oil");
                        else
                            showNotification("You traveled a greater number of kilometers than recommended without changing engine oil", "Change engine oil");
                    }
                    //end alert schimb ulei
                    //alert schim placute
                    if(x % 20 == 0) //--------------------------------prima notificare
                    {
                        int remKilo = 40000 - (kilometers - padsKm );
                        if(remKilo >=0)
                            showNotification(String.valueOf(remKilo)+" kilometers left until braking pads change","Change braking pads");
                        else
                            showNotification("You traveled a greater number of kilometers than recommended without changing breaking pads", "Change braking pads");
                    }
                    // alert schimb placute END

                    //alert la obd2
                    if(!dtc.isEmpty() )
                    {
                        String [] dtcParts = dtc.split(" ");
                        for (int i=0;i<dtcParts.length;i++)
                        {
                            checkNotification(dtcParts[i],x,carModel,carBrand,carLp,email);
                        }

                    }
                   //end aler obd
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            tvbp.setText(bp);
                            tvect.setText(ect);
                            tvfl.setText(fl);
                            tvimp.setText(imp);
                            tvmaf.setText(maf);
                            tvait.setText(ait);
                            tvtp.setText(tp);
                            tvdct.setText(dtc);
                            tvta.setText(ta);
                            tvrpm.setText(rpm);
                            tvkm.setText(km);

                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }catch (IOException | JSONException ex)
            {
                ex.printStackTrace();
            }


        }
    }

    //-------------------------------------------------------------------------------end citire

//--------------------------------------------------------------------------------------notificari
    private void showNotification(String text, String title) {
        createNotificationChannel();

        Intent intent  = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        builder.setSmallIcon(R.drawable.ic_notify);
        builder.setContentText("");
        builder.setContentTitle(title);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(text));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(++NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "My notificatoin name";
            String description = "My notification description";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    //--------------------------------------------------------------------------------------notificari END

    //
//--------------------------------------------------------------------------------------fct sa iau din baza aia fictiva intervalu

    public int getOilInterval(String model) {
        String json = "";
        try {
            InputStream is = getAssets().open("intervaloil.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i<jsonArray.length();i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                String key = obj.keys().next();
                if(key.equals(model))
                    return (obj.getInt(model));
            }

        } catch (IOException | JSONException ex)
        {
            ex.printStackTrace();
        }

        return 0;
    }
//
//--------------------------------------------------------------------------------------FCT DE INTERVAL LA OIL END##

    //--salvez erorile
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveBdErr(String name, String carModel, String carBrand, String carLp, String email)
    {
        String message = ob2Errors.get(name);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Errors").child(carLp);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtf.format(now);

        ErrorCar errorCar = new ErrorCar(carModel,carBrand,name,message,email,todayDate);

        String id = myRef.push().getKey();
        myRef.child(id).setValue(errorCar);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkNotification(String dtc, int x, String carModel, String carBrand, String carLp, String email)
    {
        if(lastSecErr.get(dtc) < 0) {
            saveBdErr(dtc, carModel, carBrand, carLp, email); //// salvez si dau push in bd
            String message = ob2Errors.get(dtc);
            showNotification(message,dtc);
            lastSecErr.put(dtc,x);
        }
        else
        {
            if(x-lastSecErr.get(dtc) < 2) //--------------------------------------------intervalu la care sa se repete notificarile
            {
                saveBdErr(dtc, carModel, carBrand, carLp, email); ///0000000000000000000000000000doar salvez in baza de date
            }
            else
            {
                saveBdErr(dtc, carModel, carBrand, carLp, email);/// salvez si dau push in bd
                String message = ob2Errors.get(dtc);
                showNotification(message,dtc);
                lastSecErr.put(dtc,x);
            }

        }
    }
    //--end salvez erorile
}
