package com.example.shivang.codex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.shivang.codex.Constant.CUR_MEETID;
import static com.example.shivang.codex.Constant.SENDER_ID;

public class SpecificMeet extends AppCompatActivity {


    private static final String TAG = "SpecificMeet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_meet);
        Bundle extras = getIntent().getExtras();
        TextView tv = (TextView) findViewById(R.id.specificActivityTV);
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_specific_meet);
        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        TextView textView = (TextView) findViewById(R.id.specificActivityTV);
        Button button=(Button)findViewById(R.id.SpecificMeetCloseButton);
        button.setBackgroundColor(Color.parseColor(textColor));

        String name = extras.getString("Name");
        String date = extras.getString("Date");
        int MID = extras.getInt("MID");
        Log.e(TAG + "name", name);
        Log.e(TAG + "date", date);
        Log.e(TAG + "MID", String.valueOf(MID));
        Constant.CurSendername = name;


        /*Button b1=(Button)findViewById(R.id.meetUpdateButton);
        Button b2=(Button)findViewById(R.id.meetDeleteButton);*/

        GroupsHelper obj = new GroupsHelper(getBaseContext());
        ArrayList<String> content = new ArrayList<>();
        String UID = obj.getUID(name);
        content = obj.getMeetContents(MID);


        MeetDataBaseHelper meetDataBaseHelper = new MeetDataBaseHelper(this);
        Meeting meeting = meetDataBaseHelper.getMeet(MID);


        //Constant.CUR_Address=content.get(4);
        Constant.CUR_Address = meeting.getAddress();
        // CUR_MEETID=content.get(0);
        CUR_MEETID = String.valueOf(MID);
        //SENDER_ID=content.get(1);
        ArrayList<Integer> RUID;
        RUID = meeting.getRuid();
        for (int i : RUID) {
            SENDER_ID = SENDER_ID + i;
        }
        Constant.LAT = meeting.getLATITUDE();
        Constant.LONG = meeting.getLONGITUDE();
        if (meeting.getLATITUDE() == null) {
            Log.e("spMeet", 446546 + " ");
            Log.e("nameMeet", meeting.getAddress());
        } else {
            Log.e("spMeet", meeting.getLATITUDE());
        }
        Log.e("checkSid", SENDER_ID);
        Constant.CurDate = String.valueOf(meeting.getDATE());
        Constant.CurDate = +meeting.getDATE().getYear() + "-" + meeting.getDATE().getMonth() + "-" + meeting.getDATE().getDate();
        Log.e("checkDATE", Constant.CurDate);
        //Constant.CUR_TIME=content.get(6);
        //Constant.CUR_TIME=String.valueOf(meeting.getTIME());
        Constant.CUR_TIME = (meeting.getTIME().getHours() + ":" + meeting.getTIME().getMinutes());
        Log.e("checkTIME", Constant.CUR_TIME);
        //Constant.CUR_MESSAGE=content.get(7);
        Constant.CUR_MESSAGE = meeting.getMESSAGE();


        tv.setText("NAME: " + name + "\n\nADDRESS: " + meeting.getAddress() + "\n\nDATE: " + (date.substring(0, 10) + " " + date.substring(30, 34)) + " \n\nTIME: " + (meeting.getTIME().getHours() + ":" + meeting.getTIME().getMinutes()) + "\n\nMESSAGE:" + meeting.getMESSAGE());


       /* b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(SpecificMeet.this,UpdateMeetActivity.class);
                startActivity(mIntent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject=new JsonObject();
                SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
                int UID=settings.getInt("UID",0);
                jsonObject.addProperty("UID",UID);
                String pass=settings.getString("PASSWORD",null);
                jsonObject.addProperty("password",pass);
                //TODO:senderID sending datatype might be wrong
                jsonObject.addProperty("ruid",Integer.parseInt(SENDER_ID));
                jsonObject.addProperty("mid",Integer.parseInt(CUR_MEETID));
                Log.e("MID",CUR_MEETID);
                Log.e("beforeJsOBsend",jsonObject.toString());
                Ion.with(getBaseContext()).load(Constant
                        .URL +Constant.DeleteMessage).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception e, String result) {
                        //

                        if (e != null) {
                            Toast.makeText(SpecificMeet.this, "Error Occurred", Toast.LENGTH_LONG).show();
                            Log.e("Signin", e.toString());
                        } else {
                            try {
                                Log.e("afterjsonsend",result);
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.length() > 0) {
                                    int msgStatus = jsonObject.getInt("status");
                                    if(msgStatus==1){

                                       GroupsHelper obj=new GroupsHelper(getBaseContext());
                                        int mid=Integer.parseInt(CUR_MEETID);
                                        obj.deleteEntryFromMeets(mid);
                                        Intent detailsActivity=new Intent(SpecificMeet.this,FuncActivity.class);
                                        detailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Toast.makeText(getApplicationContext(),"Meet DELETED", Toast.LENGTH_LONG).show();
                                        startActivity(detailsActivity);
                                    }
                                    else{
                                        Toast.makeText(SpecificMeet.this,"Meeting not deleted", Toast.LENGTH_LONG).show();
                                    }

                                }
                            } catch (Exception e1) {
                                Toast.makeText(SpecificMeet.this, "Error Occurred", Toast.LENGTH_LONG).show();
                                Log.e("Signin", e1.toString());
                            }

                        }
                    }
                });

            }
        });
    }*/
    }
    public void onSpecificMeetClose(View view) {
        Intent myIntent = new Intent(SpecificMeet.this, FuncActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
    }

}
