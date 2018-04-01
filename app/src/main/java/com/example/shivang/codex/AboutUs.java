package com.example.shivang.codex;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_about_us);


        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);


        linearLayout.setBackgroundColor(Color.parseColor(randomColor));

    }
}
