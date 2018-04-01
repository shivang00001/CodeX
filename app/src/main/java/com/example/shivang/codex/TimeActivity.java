package com.example.shivang.codex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;

public class TimeActivity extends AppCompatActivity {
    private TimePicker timePicker1;int hour,min;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        timePicker1 = (TimePicker) findViewById(R.id.timePicker);
        Button b=(Button)findViewById(R.id.button11);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_time);
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        b.setBackgroundColor(Color.parseColor(textColor));



    }
    public void onTimeOK(View view){
        if (Build.VERSION.SDK_INT >= 23 ){
            hour= timePicker1.getHour();
            min=timePicker1.getMinute();}
        else {
            hour = timePicker1.getCurrentHour();
            min = timePicker1.getCurrentMinute();
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hour",hour);
        returnIntent.putExtra("min",min);
        setResult(4,returnIntent);
        finish();

    }
}
