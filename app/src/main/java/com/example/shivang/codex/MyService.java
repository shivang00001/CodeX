package com.example.shivang.codex;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;



/**
 * Created by shivang on 31-01-2017.
 */

public class MyService extends Service {
    JsonObject jsonObject=new JsonObject();





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        int UID=settings.getInt("UID",0);
        jsonObject.addProperty("UID",UID);
        Log.e("ServiceJBEfSend",jsonObject.toString());

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            //my code
                            Ion.with(getApplicationContext()).load(Constant
                                    .URL +Constant.ServiceCheck).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

                                @Override
                                public void onCompleted(Exception e, String result) {
                                    Log.e("ServiceAfterSend",result);
                                    if (e != null) {
                                        Toast.makeText(getApplicationContext(), "retry after sometime", Toast.LENGTH_LONG).show();
                                        Log.e("service",e.toString());
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            if (jsonObject.length() > 0) {
                                                int msgStatus = jsonObject.getInt("status");
                                                JSONArray newMeet=jsonObject.getJSONArray("new");
                                                Log.e("newMeetArray",newMeet.toString());
                                                JSONArray delMeet=jsonObject.getJSONArray("delete");
                                                Log.e("delMeetArray",delMeet.toString());
                                                if(msgStatus==0){
                                                    Toast.makeText(getBaseContext(),"no meets availale",Toast.LENGTH_SHORT).show();

                                                }

                                                /*else{


                                                    intent.putExtra("delMeet",delMeet.toString());

                                                }*/

                                                if(msgStatus==1){
                                                    Intent intent = new Intent();
                                                    intent.putExtra("status",msgStatus);
                                                    intent.putExtra("newMeet",newMeet.toString());
                                                    intent.setAction("sendingbroadcast");
                                                    sendBroadcast(intent);
                                                }
                                                if(msgStatus==2){
                                                    GroupsHelper obj=new GroupsHelper(getBaseContext());
                                                    int len=delMeet.length();
                                                    for (int i=0;i<len;i++){
                                                        obj.deleteEntryFromMeets(Integer.parseInt(String.valueOf(delMeet.get(i))));
                                                    }
                                                }
                                                if(msgStatus==3){
                                                    GroupsHelper obj=new GroupsHelper(getBaseContext());
                                                    int len=delMeet.length();
                                                    Log.e("lengthOfdelMeet",len+"");
                                                    for (int i=0;i<len;i++){
                                                        Log.e("element2del",String.valueOf(delMeet.get(i)));
                                                        obj.deleteEntryFromMeets(Integer.parseInt(String.valueOf(delMeet.get(i))));
                                                        Log.e("elementDeleted",String.valueOf(delMeet.get(i)));
                                                    }
                                                    Intent intent = new Intent();
                                                    intent.putExtra("status",msgStatus);
                                                    Log.e("going2receiver",newMeet.toString());
                                                    intent.putExtra("newMeet",newMeet.toString());
                                                    intent.setAction("sendingbroadcast");
                                                    sendBroadcast(intent);

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
        timer.schedule(doAsynchronousTask, 0, 30000); //execute in every 10 ms

       // return super.onStartCommand(intent, flags, startId);

        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}




}
