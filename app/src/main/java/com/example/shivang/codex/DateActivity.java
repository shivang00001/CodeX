package com.example.shivang.codex;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import static com.example.shivang.codex.R.styleable.View;

public class DateActivity extends AppCompatActivity {
    private DatePicker datePicker;
    public Calendar calendar;
    public TextView dateView;
    public int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("datepick","case3");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        calendar = Calendar.getInstance();
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_date);
        Button b1=(Button)findViewById(R.id.button9);
        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        b1.setBackgroundColor(Color.parseColor(textColor));




    }
    public void onOK(View view){
        year = datePicker.getYear();
        month =datePicker.getMonth();
        day = datePicker.getDayOfMonth();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("year",year);
        returnIntent.putExtra("month",month+1);
        returnIntent.putExtra("day",day);
        setResult(3,returnIntent);
        finish();

    }

}
