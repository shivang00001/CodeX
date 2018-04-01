package com.example.shivang.codex;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shivang on 03-02-2017.
 */

public class MyReceiver extends BroadcastReceiver {
    JsonObject jsonObject=new JsonObject();int status;private Context context;

private String TAG="MyReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("MyReceier","reached");
         status=intent.getIntExtra("status",0);

        this.context=context;
        if(status==1||status==3){
            SharedPreferences settings = context.getSharedPreferences(SignINActivity.PREFS_NAME, 0);
            int myID=settings.getInt("UID",0);
            jsonObject.addProperty("UID",myID);
            Intent intent1=new Intent();
            String jsonArray = intent.getStringExtra("newMeet");
            Log.e("jsonArrayValue",jsonArray);
            JSONArray array=null;
            JsonArray jsonArray1=new JsonArray();
            JsonPrimitive element=null;
            try {
                 array = new JSONArray(jsonArray);
                Log.e("arrayValue",array.toString());
                Log.e("arrayLenth",array.length()+"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(int i=0;i<array.length();i++){
                try {
                     element = new JsonPrimitive(String.valueOf(array.get(i)));
                    Log.e("element",element+"");
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }
                jsonArray1.add(element);
            }
            jsonObject.add("MID",jsonArray1);
            getNewMeets(jsonObject);
        }
        /*if(status==2){
            Intent intent1=new Intent();
            String jsonArray = intent1.getStringExtra("delete");
            JSONArray array=null;
            JsonArray jsonArray1=new JsonArray();
            JsonPrimitive element=null;
            try {
                array = new JSONArray(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(int i=0;i<=array.length();i++){
                try {
                    element = new JsonPrimitive(String.valueOf(array.get(i)));
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }
                jsonArray1.add(element);
            }
            jsonObject.add("MID",jsonArray1);
            SharedPreferences settings = context.getSharedPreferences(SignINActivity.PREFS_NAME, 0);
            int myID=settings.getInt("UID",0);
            jsonObject.addProperty("UID",myID);
            getNewMeets(jsonObject);
            deleteMeets(jsonObject);
        }*/
       /* if(status==3){
            Intent intent1=new Intent();
            String jsonArray = intent1.getStringExtra("newMeet");
            JSONArray array=null;
            JsonArray jsonArray1=new JsonArray();
            JsonPrimitive element=null;
            try {
                array = new JSONArray(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(int i=0;i<=array.length();i++){
                try {
                    element = new JsonPrimitive(String.valueOf(array.get(i)));
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }
                jsonArray1.add(element);
            }
            jsonObject.add("MID",jsonArray1);
            SharedPreferences settings = context.getSharedPreferences(SignINActivity.PREFS_NAME, 0);
            int myID=settings.getInt("UID",0);
            jsonObject.addProperty("UID",myID);
            getNewMeets(jsonObject);
            //deleteMeets(jsonObject);
        }*/

    }

    public  void getNewMeets(JsonObject jsonObject){
        Log.e("beforeSend"+TAG,jsonObject.toString());
        Ion.with(context).load(Constant
                .URL +Constant.FetchMeet).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

            @Override
            public void onCompleted(Exception e, String result) {
                Log.e("afeterSend"+TAG,result);
                if (e != null) {
                    Toast.makeText(context, "Error Occurred", Toast.LENGTH_LONG).show();
                    Log.e("Signin", e.toString());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.length() > 0) {
                            int msgStatus = jsonObject.getInt("status");
                            if(msgStatus==1){

                                JSONArray jsonArray;
                                jsonArray=jsonObject.getJSONArray("messages");
                                for(int i=0;i<jsonArray.length();i++){
                                JSONObject meets=jsonArray.getJSONObject(i);
                                    //fetching data of meeting from sever
                                    String lat=meets.getString("latitude");
                                    String lon=meets.getString("longitude");
                                    String address=meets.getString("address");
                                    String meet_date=meets.getString("meet_date");
                                    String meet_time=meets.getString("meet_time");
                                    String messg=meets.getString("message");
                                    String SUID=meets.getString("suid");
                                    int meetID=meets.getInt("mid");

                                    int senderID=Integer.parseInt(SUID);
                                    ArrayList<Integer> sendID=new ArrayList<>();
                                    sendID.add(senderID);

                                    //converting date and time
                                    Date date=new SimpleDateFormat("yyyy-MM-dd").parse(meet_date);
                                    Date time=new SimpleDateFormat("HH:mm").parse(meet_time);
                                   //create a new meet
                                    Meeting meeting=new Meeting();
                                    meeting.setID(meetID);
                                    meeting.setRuid(sendID);
                                    meeting.setAddress(address);
                                    meeting.setDATE(date);
                                    meeting.setTIME(time);
                                    meeting.setMESSAGE(messg);
                                    meeting.setIS_MY(1);
                                    meeting.setLATITUDE(lat);
                                    meeting.setLONGITUDE(lon);
                                    //add meeting to database
                                    MeetDataBaseHelper meetDataBaseHelper=new MeetDataBaseHelper(context);
                                    meetDataBaseHelper.addMeet(meeting);

                                    GroupsHelper obj=new GroupsHelper(context);
                                    String senderName=obj.getSenderName(SUID);

                                    NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
                                    builder.setSmallIcon(R.drawable.cast_ic_notification_1);
                                    builder.setContentText("You have been CODEXed");
                                    builder.setContentTitle("Meet Request from "+senderName);
                                    Intent notifIntent=new Intent(context,CurrentMeetNotif.class);

                                    //notifIntent.putExtra("meetiD",meetID);
                                    notifIntent.putExtra("senderName",senderName);
                                    notifIntent.putExtra("date",meet_date);
                                    notifIntent.putExtra("time",meet_time);
                                    notifIntent.putExtra("address",address);
                                    notifIntent.putExtra("message",messg);

                                    TaskStackBuilder stackBuilder= TaskStackBuilder.create(context);
                                    stackBuilder.addParentStack(NotifActivity.class);
                                    stackBuilder.addNextIntent(notifIntent);
                                    PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                                    builder.setContentIntent(pendingIntent);
                                    NotificationManager NM= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    NM.notify(0,builder.build());

                                   // obj.addEntries2MeetTable(meetID,senderID,address,meet_date,meet_time,messg);

                                }





                            }
                            else{
                               // Toast.makeText(context,"Login Unsuccessful", Toast.LENGTH_LONG).show();
                                Log.e("receiver.getNewMeets","no meets");
                            }

                        }
                    } catch (Exception e1) {
                        //Toast.makeText(context, "Error Occurred", Toast.LENGTH_LONG).show();
                        Log.e("receiverError", e1.toString());
                    }

                }
            }
        });
    }


    /*public void deleteMeets(JsonObject jsonObject){
        Log.e("beforeSendDel"+TAG,jsonObject.toString());

        Ion.with(context).load(Constant
                .URL +Constant.FetchDeleteMeet).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

            @Override
            public void onCompleted(Exception e, String result) {
                Log.e("AfterSendDel"+TAG,result);
                if (e != null) {
                    //Toast.makeText(context, "Error Occurred", Toast.LENGTH_LONG).show();
                    Log.e("fetchDeleteMeet", e.toString());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.length() > 0) {
                            int msgStatus = jsonObject.getInt("status");
                            if(msgStatus==1){

                                JSONArray jsonArray=new JSONArray();
                                jsonArray=jsonObject.getJSONArray("mids");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject meets=jsonArray.getJSONObject(i);
                                    int  mid=meets.getInt("mid");
                                    GroupsHelper obj=new GroupsHelper(context);
                                    String senderName=obj.getSenderNameFromMeet(mid);
                                    obj.deleteEntryFromMeets(mid);
                                    NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
                                    builder.setSmallIcon(R.drawable.cast_ic_notification_1);
                                    builder.setContentText("You have been CODEXed");
                                    builder.setContentTitle("Meet deleted from "+senderName);
                                    Intent notifIntent=new Intent(context,NotifActivity.class);
                                    TaskStackBuilder stackBuilder= TaskStackBuilder.create(context);
                                    stackBuilder.addParentStack(NotifActivity.class);
                                    stackBuilder.addNextIntent(notifIntent);
                                    PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                                    builder.setContentIntent(pendingIntent);
                                    NotificationManager NM= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    NM.notify(0,builder.build());

                                    obj.deleteEntryFromMeets(mid);

                                }





                            }
                            else{
                                // Toast.makeText(context,"Login Unsuccessful", Toast.LENGTH_LONG).show();
                                Log.e("receiver.getNewMeets","no meets");
                            }

                        }
                    } catch (Exception e1) {
                        //Toast.makeText(context, "Error Occurred", Toast.LENGTH_LONG).show();
                        Log.e("receiverError", e1.toString());
                    }

                }
            }
        });

    }*/


}
