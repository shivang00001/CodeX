package com.example.shivang.codex;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shivang on 28-04-2017.
 */

public class ServiceLocationFetch extends Service {

    JsonObject jsonObject=new JsonObject();
    String TAG="ServiceLocationFetch";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        Bundle extras=intent.getExtras();
        String ruid=extras.getString("ruid");
        final int uid=settings.getInt("UID",0);
        final String password=settings.getString("PASSWORD",null);
        final JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("UID",uid);
        jsonObject.addProperty("password",password);
        jsonObject.addProperty("ruid",ruid);

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
                                    .URL +Constant.LOCATIONFETCH).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

                                @Override
                                public void onCompleted(Exception e, String result) {
                                    Log.e(TAG+"bnd",result);
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
                                                    String latitude=jsonObject.getString("latitude");
                                                    String longitude=jsonObject.getString("longitude");
                                                    Constant.LATITUDE=latitude;
                                                    Constant.LONGITUDE=longitude;

                                                }
                                                if(msgStatus==0){
                                                    Toast.makeText(getApplicationContext(),"Person has not permitted to view hos position",Toast.LENGTH_SHORT).show();
                                                    stopSelf();
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
