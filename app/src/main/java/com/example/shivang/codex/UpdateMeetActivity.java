package com.example.shivang.codex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.shivang.codex.Constant.CUR_Address;
import static com.example.shivang.codex.Constant.CUR_MEETID;
import static com.example.shivang.codex.Constant.CUR_MESSAGE;
import static com.example.shivang.codex.Constant.CUR_TIME;
import static com.example.shivang.codex.Constant.CurDate;
import static com.example.shivang.codex.Constant.MSG;
import static com.example.shivang.codex.Constant.SENDER_ID;

public class UpdateMeetActivity extends AppCompatActivity {
    Button b1, b2, b3,b4;
    int PlacePickerRequest = 1;
    int messageRequest = 2;
    int dateRequest = 3;
    int timeRequest = 4;
    JsonArray jsonArray=new JsonArray();
    private String TAG="UpdateMeetActivity";
    String[] suid=new String[1000];Meeting m;
    String address,mssg,curDate,curTime,latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meet);

        b1 = (Button) findViewById(R.id.updatePlaceButton);
        b2 = (Button) findViewById(R.id.updateDateButton);
        b3 = (Button) findViewById(R.id.updateTimeButton);
        b4 = (Button) findViewById(R.id.updateMessageButton);


    }

    public void onUpdatePlace(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent = builder.build(UpdateMeetActivity.this);
        startActivityForResult(intent, PlacePickerRequest);
    }

    public void onUpdateDate(View view) {
        Intent intent = new Intent(getApplicationContext(), DateActivity.class);
        startActivityForResult(intent, dateRequest);
    }

    public void onUpdateTime(View view) {
        Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
        startActivityForResult(intent, timeRequest);
    }
    public void onUpdateMsg(View view) {
        Intent intent = new Intent(getApplicationContext(), WriteMessageActvity.class);
        startActivityForResult(intent, messageRequest);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                Log.i("PlacePickerActivity", "case1");
                Place place = PlacePicker.getPlace(this, data);
                 address = place.getAddress().toString();
                LatLng latLng = place.getLatLng();
                latitude = String.valueOf(latLng.latitude);
                longitude = String.valueOf(latLng.longitude);

                break;
            case 2:
                Log.i("PlacePickerActivity", "case2");
                 mssg= data.getStringExtra("message");
                /*if(mssg.isEmpty()){
                    mssg=CUR_MESSAGE;
                }else{
                    CUR_MESSAGE=mssg;
                }*/

                Constant.MEMO = data.getStringExtra("memo");
                break;

            case 3:
                Log.i("date", "case 3");
                curDate = data.getIntExtra("year", 0) + "-" + data.getIntExtra("month", 0) + "-" + data.getIntExtra("day", 0);
               /* if(curDate.isEmpty()) {
                    curDate=Constant.CurDate;
                }
                else{
                   Constant.CurDate=curDate;
                }*/
                break;
            case 4:
                curTime = data.getIntExtra("hour", 0) + ":" + data.getIntExtra("min", 0);
               /* if(curTime.isEmpty()) {
                    curTime=Constant.CUR_TIME;
                }
                else{
                    Constant.CUR_TIME=curTime;
                }*/
                break;

        }
    }
    public void onUpdate(View view){
        MeetDataBaseHelper meetDataBaseHelper=new MeetDataBaseHelper(this);
        meetDataBaseHelper.print("MY_MEETS");
        JsonObject jsonObject=new JsonObject();
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        int UID=settings.getInt("UID",0);
        jsonObject.addProperty("UID",UID);
        String pass=settings.getString("PASSWORD",null);
        jsonObject.addProperty("password",pass);
        if(latitude==null){
            jsonObject.addProperty("latitude",Double.parseDouble(Constant.LAT));
        }else {
            jsonObject.addProperty("latitude",Double.parseDouble(latitude));
        }
        if(longitude==null){
            jsonObject.addProperty("longitude",Double.parseDouble(Constant.LONG));
        }else {
            jsonObject.addProperty("longitude",Double.parseDouble(longitude));
        }


        if(address==null){
            jsonObject.addProperty("address",CUR_Address);
        }else {
            jsonObject.addProperty("address",address);
        }

        if(curDate==null){
            jsonObject.addProperty("meet_date",CurDate);
        }else {
            jsonObject.addProperty("meet_date",curDate);
        }
        if(curTime==null){
            jsonObject.addProperty("meet_time",CUR_TIME);
        }else {
            jsonObject.addProperty("meet_time",curTime);
        }
        jsonObject.addProperty("suid",UID);
         suid=SENDER_ID.split(" ");
        for(String x:suid){
            int i=Integer.parseInt(x);
            Log.e("chSIDINUPDATE",i+"");
            JsonPrimitive element= new JsonPrimitive(i);
            jsonArray.add(element);
        }
        jsonObject.add("ruid",jsonArray.getAsJsonArray());
        if(mssg==null){
            jsonObject.addProperty("message",CUR_MESSAGE);
        }else {
            jsonObject.addProperty("message",mssg);
        }

        //jsonObject.addProperty("message",MSG);
        Log.e(TAG+"MSG?:",MSG);
        jsonObject.addProperty("mid",CUR_MEETID);
        Log.e(TAG,jsonObject.toString());
        Log.e(TAG,jsonArray.toString());
        Ion.with(this).load(Constant
                .URL +Constant.MessageUpdate).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

            @Override
            public void onCompleted(Exception e, String result) {
                Log.e(TAG,result.toString());
                if (e != null) {
                    Toast.makeText(UpdateMeetActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                    Log.e("Signin", e.toString());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.length() > 0) {
                            int msgStatus = jsonObject.getInt("status");
                            if(msgStatus==2){
                                String returnMessage=jsonObject.getString("MID");
                                int mid=Integer.parseInt(returnMessage);
                                createAmeet(mid);
                                Log.e(TAG,"createAMeet");
                                MeetDataBaseHelper obj=new MeetDataBaseHelper(getBaseContext());
                                GroupsHelper gh=new GroupsHelper(getBaseContext());
                                obj.addMeet(m);
                                Log.e(TAG,"meeting added");
                                int meetID=Integer.parseInt(CUR_MEETID);
                                obj.deleteEntryFromMeets(meetID);
                                Log.e(TAG,"meeting deleted");
                                MeetDataBaseHelper meetDataBaseHelper=new MeetDataBaseHelper(getBaseContext());

                                meetDataBaseHelper.print("MY_MEETS");

                               /* int senderID=Integer.parseInt(SENDER_ID);
                                //obj.addEntries2MeetTable(mid,senderID,CUR_Address,CurDate,CUR_TIME,MSG);
                                int meetID=Integer.parseInt(CUR_MEETID);
                                obj.deleteEntryFromMeets(meetID);*/

                                Log.d("sa","2");

                                Intent detailsActivity=new Intent(UpdateMeetActivity.this,FuncActivity.class);
                                detailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(UpdateMeetActivity.this,"Meeting Updated", Toast.LENGTH_LONG).show();
                                startActivity(detailsActivity);
                            }
                            else{
                                Toast.makeText(UpdateMeetActivity.this,"Login Unsuccessful", Toast.LENGTH_LONG).show();
                            }

                        }
                    } catch (Exception e1) {
                        Toast.makeText(UpdateMeetActivity.this, e1.toString(), Toast.LENGTH_LONG).show();
                        Log.e("Signin", e1.toString());
                    }

                }
            }
        });
    }

    private void createAmeet(int mid) {
        m=new Meeting();
        m.setID(mid);
        Log.e("crteMeet",mid+"");
        ArrayList<Integer> ruid=new ArrayList<>();
        for(String x:suid)
        {
            Log.e("crteMeet",x);
            ruid.add(Integer.parseInt(x));
        }
        Log.e("crteMeet",ruid.toString());
        m.setRuid(ruid);
        if(latitude==null){
            m.setLATITUDE(Constant.LAT);
        }else{
            m.setLATITUDE(latitude);
        }
        if(longitude==null){
            m.setLONGITUDE(Constant.LONG);
        }else{
            m.setLONGITUDE(longitude);
        }

        Log.e("crteMeet",CUR_Address);
        if(address==null){
            m.setAddress(CUR_Address);
        }
        else{
            m.setAddress(address);
        }
        if(curDate==null) {
            try {
                m.setDATE(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(CurDate));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        else {
            try {
                m.setDATE(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(curDate));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        Log.e("crteMeet",CUR_Address);
        if(curTime==null) {
            try {
                m.setTIME(new SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(CUR_TIME));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        else{
            try {
                m.setTIME(new SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(curTime));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        Log.e("crteMeet",CUR_TIME);
        if(mssg==null) {
            m.setMESSAGE(CUR_MESSAGE);
        }else{
            m.setMESSAGE(mssg);
        }
        Log.e("crteMeet",CUR_MESSAGE);
        m.setIS_MY(0);

    }
}