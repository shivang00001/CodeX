package com.example.shivang.codex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrentMeetNotif extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_meet_notif);
        Bundle extras=getIntent().getExtras();
        String name=extras.getString("senderName");
        String address=extras.getString("address");
        String date=extras.getString("date");
        String time=extras.getString("time");
        String message=extras.getString("message");

        TextView tv=(TextView)findViewById(R.id.notifTextView);
        tv.setText("NAME: "+name+"\nADDRESS: "+address+"\nDATE: "+date+"\nTIME: "+time+"\nMESSAGE: "+message);

    }
}
