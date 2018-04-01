package com.example.shivang.codex;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shivang on 28-04-2017.
 */

public class ServiceLocationUpdate extends Service {
    JsonObject jsonObject=new JsonObject();
    String TAG="ServiceLocationUpdate";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
         int uid=settings.getInt("UID",0);
         int permission=settings.getInt("Permission",0);
        jsonObject.addProperty("UID",uid);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(getApplicationContext(),"You did not gave me permission",Toast.LENGTH_SHORT).show();
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String longitude = String.valueOf(location.getLongitude());
        String latitude =String.valueOf(location.getLatitude()) ;
        jsonObject.addProperty("latitude",latitude);
        jsonObject.addProperty("longitude",longitude);
        jsonObject.addProperty("permission",permission);

        Log.e("ServiceJBEfSend",jsonObject.toString());

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            Ion.with(getApplicationContext()).load(Constant
                                    .URL +Constant.LOCATIONUPDATE).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

                                @Override
                                public void onCompleted(Exception e, String result) {
                                    Log.e(TAG,result);
                                    //dismissProgressBar();
                                    if (e != null) {
                                        Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                                        Log.e("Signin", e.toString());
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            if (jsonObject.length() > 0) {
                                                int msgStatus = jsonObject.getInt("status");
                                                if(msgStatus==1){
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"Login Unsuccessful", Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        } catch (Exception e1) {
                                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                                            Log.e("Signin", e1.toString());
                                        }

                                    }
                                }
                            });


                            Log.i("Service","Execution");
                            //Toast.makeText(getApplicationContext(),"Service1",Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {e.printStackTrace();
                        }
                    }
                });
            }
        };
        Log.i("Service","Execution1");
        timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 3sec
        return Service.START_STICKY;
    }

}
