package com.example.shivang.codex;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
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
 * Created by shivang on 26-02-2017.
 */

public class SplashScreen extends AppCompatActivity {

    private static int timer=3000;int countContacts;
    ArrayList<String>nameArray=new ArrayList<>();
    ArrayList<String>numberArray=new ArrayList<>();
    JsonArray sendArray=new JsonArray();
    GroupsHelper obj=new GroupsHelper(getBaseContext());
    JsonObject jsonObject1=new JsonObject();
    private String TAG="SplashScreen";



    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
                boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
                if(hasLoggedIn) {

                    Intent i = new Intent(SplashScreen.this, FuncActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i = new Intent(SplashScreen.this, SignINActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, timer);

    }
}
