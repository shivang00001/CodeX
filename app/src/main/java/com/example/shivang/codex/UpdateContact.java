package com.example.shivang.codex;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shivang on 25-04-2017.
 */

public class UpdateContact extends AsyncTask<Void,Void,Void> {
    Context context;
    int countContacts;
    private String TAG="UpdateContact";
    ArrayList<String>nameArray=new ArrayList<>();
    ArrayList<String>numberArray=new ArrayList<>();
    JsonArray sendArray=new JsonArray();
    JsonObject jsonObject1=new JsonObject();

    UpdateContact (Context context){
        this.context=context;
    }

    /*@Override
    protected void onPostExecute(Object o) {
        Constant.aSyncFlag=true;

    }*/

   /* @Override
    protected Object doInBackground(Object[] params) {

        //is cancelled in loop
        if(this.isCancelled()==true){
            doInBackground();
        }
        SharedPreferences settings = context.getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        final int count = settings.getInt("countContacts", 0);
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        do {

            countContacts++;

        } while (phones.moveToNext());
        Log.d(TAG, "" + countContacts + "/" + count);
        int newContacts = countContacts - count;
        if (countContacts > count) {
            Cursor cur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
            int k = 1;
            Log.d(TAG, "" + newContacts);
            while (k <= newContacts && cur.moveToNext()) {
                try {
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    nameArray.add(name);
                    String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    numberArray.add(phoneNumber);
                    k++;
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
            for (String mobile : numberArray) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("mobile", mobile);
                sendArray.add(jsonObject);
            }
            jsonObject1.addProperty("UID", 33);
            jsonObject1.addProperty("password", "r");
            jsonObject1.add("contacts", sendArray.getAsJsonArray());

            if (numberArray.contains("8888822222") || nameArray.contains("dummy"))
                Log.d(TAG + " MOB", "problem");

            Log.d(TAG + " SEND_JSON", jsonObject1.toString());
            Ion.with(context).load(Constant.URL + Constant.CONTACT).setTimeout(20000).setJsonObjectBody(jsonObject1).asString().setCallback(new FutureCallback<String>() {
                public void onCompleted(Exception e, String result) {

                    if (e != null) {
                        Toast.makeText(context, "Error Occurred", Toast.LENGTH_LONG).show();
                        //what to do
                    } else {
                        try {
                            int msgStatus = 0;
                            Log.d(TAG + " SEND", result);
                            JSONObject jsonObject = new JSONObject(result);
                            Log.d(TAG + "SEND", jsonObject.toString());
                            if (jsonObject.length() > 0) {
                                try {
                                    msgStatus = jsonObject.getInt("status");
                                } catch (Exception e1) {
                                    Log.e(TAG, e1.toString());
                                }
                                if (msgStatus == 1) {

                                    JSONArray rec_array = jsonObject.getJSONArray("contacts");
                                    int length = rec_array.length();
                                    GroupsHelper groupsHelper = new GroupsHelper(context);
                                    for (int i = 0; i < length; i++) {
                                        String name = "", number = "", UID = "";
                                        try {
                                            JSONObject obj = rec_array.getJSONObject(i);
                                            number = obj.getString("mobile");
                                            UID = obj.getString("UID");
                                        } catch (Exception e2) {
                                            Log.e(TAG, e.toString());
                                        }

                                        Log.d(TAG + " UID", UID);
                                        Log.d(TAG + " NUMBER", number);

                                        ContentResolver cr = context.getContentResolver();
                                        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                                null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
                                        while (cur.moveToNext()) {
                                            try {
                                                String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                                if (phoneNumber.equals(number)) {
                                                    name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                                    Log.d(TAG + " NAME", name);
                                                    groupsHelper.addPersonToContact(UID, name, number);
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }

                                    }

                                   /* Intent myIntent=new Intent(getApplicationContext(),AppSetupActiviy.class);
                                    startActivity(myIntent);

                                    //rec_array=jsonObject.get(contacts);
                                } else {
                                    Toast.makeText(context, "data cannot be sent", Toast.LENGTH_LONG).show();
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
        return null;
        }*/
    @Override
    protected Void doInBackground(Void...params){
        return null;
    }
}
