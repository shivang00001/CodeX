package com.example.shivang.codex;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.shivang.codex.SignINActivity.PREFS_NAME;


public class PlacePickerActivity extends NavClass {
    int PlacePickerRequest = 1;
    int messageRequest = 2;
    int dateRequest = 3;
    int timeRequest = 4;
    JsonObject jsonObject;
    private String TAG="PlacePickerActivity";

    public String lat, lng;
    Intent disp;int val=2;int ch=0;
    ProgressDialog progressDialog;
    String senderChoiceArray[]={"Single Person","Group"};boolean omtc=false;

    String address,meet_date,meet_time;ArrayList<String>senderUID=new ArrayList<>();String msg,msg1;
    ArrayList<Integer>ruid=new ArrayList<>();

    String randomColor,textColor;String addressCheck,messageCheck,dateCheck,timeCheck;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_place_picker);
        disp=new Intent(PlacePickerActivity.this,DetailsActivity.class);
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
         randomColor = settings.getString("backgroundColor", null);
         textColor = settings.getString("buttonColor", null);

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_place_picker);
        linearLayout.setBackgroundColor(Color.parseColor(randomColor));



    }


    public void onPickPlace(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent intent = builder.build(PlacePickerActivity.this);
            startActivityForResult(intent, PlacePickerRequest);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"No Internet!",Toast.LENGTH_SHORT).show();
            Log.d(TAG,e.toString());
        }


    }

    public void onMessageSend(View view) {
        Intent intent = new Intent(getApplicationContext(), WriteMessageActvity.class);
        startActivityForResult(intent, messageRequest);

    }

    public void onPickDaTim(View view) {
        Intent intent = new Intent(getApplicationContext(), DateActivity.class);
        startActivityForResult(intent, dateRequest);
    }

    public void onPickTime(View view) {
        Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
        startActivityForResult(intent, timeRequest);
    }





    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                Log.i("PlacePickerActivity","case1");
                     Place place = PlacePicker.getPlace(this,data);
                     Log.d("PlacePicker",place.getAddress().toString());
                     String address =  place.getAddress().toString();
                     LatLng latLng=place.getLatLng();
                     lat=String.valueOf(latLng.latitude);
                     lng=String.valueOf(latLng.longitude);
                     disp.putExtra("latitude",lat);
                     disp.putExtra("longitude",lng);
                     disp.putExtra("address",address);
                    addressCheck=address;
                     Button button4=(Button)findViewById(R.id.button4);
                     button4.setBackgroundColor(Color.parseColor(textColor));
                     break;
            case 2:
                     Log.i("PlacePickerActivity","case2");
                     disp.putExtra("message",data.getStringExtra("message"));
                     messageCheck=data.getStringExtra("message");
                     disp.putExtra("memo",data.getStringExtra("memo"));
                     Button button5=(Button)findViewById(R.id.button5);
                     button5.setBackgroundColor(Color.parseColor(textColor));
                     break;
            case 3:
                Log.i("date","case 3");
                disp.putExtra("year",data.getIntExtra("year",0));
                disp.putExtra("month",data.getIntExtra("month",0));
                disp.putExtra("day",data.getIntExtra("day",0));
                dateCheck=String.valueOf(data.getIntExtra("year",0)+data.getIntExtra("month",0)+data.getIntExtra("day",0));

                Button button8=(Button)findViewById(R.id.button8);
                button8.setBackgroundColor(Color.parseColor(textColor));
                break;
            case 4:
                disp.putExtra("hour",data.getIntExtra("hour",0));
                disp.putExtra("min",data.getIntExtra("min",0));
                timeCheck=String.valueOf(data.getIntExtra("hour",0)+data.getIntExtra("min",0));
                Button button10=(Button)findViewById(R.id.button10);
                button10.setBackgroundColor(Color.parseColor(textColor));
                break;
            case 5:
                JsonArray jsonArray=new JsonArray();
                ArrayList<String> UID;
                UID=data.getStringArrayListExtra("receiverArray");
                for(String s:UID){
                    senderUID.add(s);
                    int uid=Integer.parseInt(s);
                    ruid.add(uid);
                    JsonPrimitive element=new JsonPrimitive(uid);
                    jsonArray.add(element);
                }
                jsonObject.add("ruid",jsonArray.getAsJsonArray());
                 msg = disp.getStringExtra("message");
                jsonObject.addProperty("message",msg);
                send(jsonObject);
                break;
            case 6:
                ArrayList<String> uid_Grp;
                uid_Grp=data.getStringArrayListExtra("receiverArray");
                JsonArray jsonArray_1=new JsonArray();
                for(String s:uid_Grp){
                    senderUID.add(s);
                    int uid=Integer.parseInt(s);
                    ruid.add(uid);
                    JsonPrimitive element=new JsonPrimitive(uid);
                    jsonArray_1.add(element);
                }
                jsonObject.add("ruid",jsonArray_1.getAsJsonArray());
                 msg = disp.getStringExtra("message");
                jsonObject.addProperty("message",msg);
                send(jsonObject);
                break;
        }
    }

    /*public void onDisplay(View view){

            startActivity(disp);
            Button button7=((Button)findViewById(R.id.button7));
            button7.setBackgroundColor(Color.parseColor("#07575B"));

    }*/

    public void setupProgressBar(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending...");
        progressDialog.show();
    }

    public void dismissProgressBar(){
        progressDialog.dismiss();
    }


    private void send(final JsonObject jsonObject){
        Log.e(TAG+"bnd",jsonObject.toString());
        Ion.with(this).load(Constant
                .URL +Constant.SERVERMEET).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

            @Override
            public void onCompleted(Exception e, String result) {
               // Log.e(TAG+"bnd",result);
                //dismissProgressBar();
                if (e != null) {
                    Toast.makeText(PlacePickerActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                    Log.e("Signin", e.toString());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.length() > 0) {
                            int msgStatus = jsonObject.getInt("status");
                            if(msgStatus==2){
                                int mid=jsonObject.getInt("MID");
                                putStatusOfMeet(mid);
                                Log.e(TAG,result);
                                //String returnMessage=jsonObject.getString("message");
                                Intent detailsActivity=new Intent(PlacePickerActivity.this,FuncActivity.class);
                                detailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //Toast.makeText(PlacePickerActivity.this,returnMessage, Toast.LENGTH_LONG).show();
                                startActivity(detailsActivity);
                            }
                            else{
                                Toast.makeText(PlacePickerActivity.this,"Login Unsuccessful", Toast.LENGTH_LONG).show();
                            }

                        }
                    } catch (Exception e1) {
                        Toast.makeText(PlacePickerActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                        Log.e("Signin", e1.toString());
                    }

                }
            }
        });


    }

    private void putStatusOfMeet(int mid) {
        Log.e(TAG,"puttingStatus");
        Meeting obj=new Meeting();
        obj.setID(mid);
        obj.setRuid(ruid);
        obj.setAddress(address);
        obj.setLATITUDE(lat);
        Log.e("latCheck",obj.getLATITUDE());
        obj.setLONGITUDE(lng);
        Date meetDate=null;
        try {
            meetDate = new SimpleDateFormat("yyyy-MM-dd").parse(meet_date);
        }catch (Exception e){Log.e(TAG,e.toString());}
        obj.setDATE(meetDate);
        Date meetTime=null;
        try {
            meetTime = new SimpleDateFormat("HH:mm").parse(meet_time);
        }catch (Exception e){Log.e(TAG,e.toString());}
        obj.setTIME(meetTime);
        obj.setMESSAGE(msg);
        // 0 if i have created a meeting else 1: value of IS_MY
        obj.setIS_MY(0);
        MeetDataBaseHelper meetDataBaseHelper=new MeetDataBaseHelper(this);
        Log.e(TAG,mid+"");
        Log.e(TAG,ruid+"");
        Log.e(TAG,address+"");
        Log.e(TAG,meetDate+"");
        Log.e(TAG,meetTime+"");
        Log.e(TAG,msg+"");

        meetDataBaseHelper.addMeet(obj);
        Log.e("point reached","reached");

    }


    public void onMeetProceed(View view) {
        //setting progress dialogue
        if(addressCheck==null){
            Toast.makeText(getApplicationContext(),"Choose Place to Send",Toast.LENGTH_SHORT).show();
            finish();
        }
        if(dateCheck==null){
            Toast.makeText(getApplicationContext(),"Choose Date of Meeting",Toast.LENGTH_SHORT).show();
            finish();
        }
        if(timeCheck==null){
            Toast.makeText(getApplicationContext(),"Choose Time of Meeting",Toast.LENGTH_SHORT).show();
            finish();
        }
        if(messageCheck==null){
            Toast.makeText(getApplicationContext(),"Send Some Message,don't be shy!!",Toast.LENGTH_LONG).show();
            finish();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(PlacePickerActivity.this);
        builder.setTitle("select sender");
        Log.d("PlacePicket","errPoint");

        builder.setItems(senderChoiceArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ch=1;
                      // setupProgressBar();
                         jsonObject = new JsonObject();
                         jsonObject=getInfoToSend(jsonObject,ch);
                        break;
                    case 1:
                        ch=2;
                        //setupProgressBar();
                        jsonObject = new JsonObject();
                        jsonObject=getInfoToSend(jsonObject,ch);
                        break;
                }

            }
        });
        Log.d("AfterListener","fds");
        builder.create().show();
    }

        private JsonObject getInfoToSend(JsonObject jsonObject,int ch) {


        Double lng = disp.getDoubleExtra("longitude",0.0);
        Double lat = disp.getDoubleExtra("latitude",0.0);



         address = disp.getStringExtra("address");

        int date = disp.getIntExtra("day", 00);
        String day = Integer.toString(date);
        int mon = disp.getIntExtra("month", 00);
        String month = Integer.toString(mon);
        int yr = disp.getIntExtra("year", 00);
        String year = Integer.toString(yr);

         meet_date = year + "-" + month + "-" + day;

        int hr = disp.getIntExtra("hour", 00);
        int mn = disp.getIntExtra("min", 00);
        String hour = Integer.toString(hr);
        String min = Integer.toString(mn);

         meet_time = hour + ":" + min;

        String memo = disp.getStringExtra("memo");

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        int UID = settings.getInt("UID", 0);
        String password = settings.getString("PASSWORD", null);

            jsonObject.addProperty("UID",UID);
            jsonObject.addProperty("password",password);
            jsonObject.addProperty("latitude",lat);
            jsonObject.addProperty("longitude",lng);
            jsonObject.addProperty("address",address);
            jsonObject.addProperty("meet_date",meet_date);
            jsonObject.addProperty("meet_time",meet_time);
            jsonObject.addProperty("suid",UID);

            if(ch==1) {
                Log.d(TAG,"before receiver 1");
                Intent intent = new Intent(PlacePickerActivity.this, SelectReceiver.class);
                startActivityForResult(intent, 5);
            }
            else{
                Intent intent = new Intent(PlacePickerActivity.this, SelectReceiverGroup.class);
                startActivityForResult(intent, 6);

            }


            return jsonObject;


    }

    /*GroupsHelper obj = new GroupsHelper(getBaseContext());
        obj.addEntries2MeetTable(address, meet_date, meet_time, msg);
        obj.addEntries2MemoTable(memo);*/
}
