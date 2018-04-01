package com.example.shivang.codex;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.JSONArrayBody;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class AppSetupActiviy extends AppCompatActivity {


    private static final String TAG ="AppSetup" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setup_activiy);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        int uid = settings.getInt("UID", 0);

        String password = settings.getString("PASSWORD", null);



         JsonObject jsonObject1=new JsonObject();
         JsonArray contacts = new JsonArray();

        //TODO: hardcoded
        jsonObject1.addProperty("UID",33);
        jsonObject1.addProperty("password","r");

        ContentResolver cr = getContentResolver();

        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        int i = 0;
        while (cur.moveToNext()) {
            JsonObject jsonObject = new JsonObject();
            String number = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            jsonObject.addProperty("mobile", number);
            contacts.add(jsonObject);
        }

        jsonObject1.add("contacts",contacts.getAsJsonArray());
        //jsonObject1.addProperty("contacts", String.valueOf(contacts));
        Log.d(TAG+" BEF SEND",jsonObject1.toString());
        Ion.with(this).load(Constant
                .URL + Constant.CONTACT).setTimeout(20000).setJsonObjectBody(jsonObject1).asString().setCallback(new FutureCallback<String>() {

            @Override
            public void onCompleted(Exception e, String result) {
                progressDialog.dismiss();
                if (e != null) {
                    Toast.makeText(AppSetupActiviy.this, "Error Occurred", Toast.LENGTH_LONG).show();
                    //what to do
                } else {
                    try {

                        Log.d("AFTER SENDING",result);
                        JSONObject jsonObject = new JSONObject(result);
                        Log.d("AFTER SENDING",jsonObject.toString());
                        if (jsonObject.length() > 0) {
                            int msgStatus = jsonObject.getInt("status");
                            if(msgStatus==1){

                                JSONArray rec_array=jsonObject.getJSONArray("contacts");
                                int length=rec_array.length();
                                GroupsHelper groupsHelper=new GroupsHelper(getBaseContext());
                                for(int  i=0;i<length;i++){
                                    String name="";
                                    JSONObject obj=rec_array.getJSONObject(i);
                                    String number=obj.getString("mobile");
                                    String UID=obj.getString("UID");

                                    Log.d("UIDreceiver",UID);
                                    Log.d("NUMBERreceiver",number);

                                    ContentResolver cr = getContentResolver();
                                    Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                            null, null,  "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
                                    while (cur.moveToNext()) {
                                        try {
                                            String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                            if (phoneNumber.equals(number)) {
                                                name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                                Log.d("NAME", name);
                                                groupsHelper.addPersonToContact(UID, name, number);
                                            }
                                        }catch(Exception ex){}
                                    }

                                }
                                Intent myIntent=new Intent(getApplicationContext(),FuncActivity.class);
                                startActivity(myIntent);

                                //rec_array=jsonObject.get(contacts);
                            }
                            else{
                                Toast.makeText(AppSetupActiviy.this, "data cannot be sent", Toast.LENGTH_LONG).show();
                                //what to do
                            }
                        }

                    } catch (JSONException e1) {
                        //what to do
                        e1.printStackTrace();
                    }
                }
            }
        });


    }
}














