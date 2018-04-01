package com.example.shivang.codex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_group);
        Button b1=(Button)findViewById(R.id.makegroupbutton);
        Button b2=(Button)findViewById(R.id.showgroupButton);

        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);


        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        b1.setBackgroundColor(Color.parseColor(textColor));
        b2.setBackgroundColor(Color.parseColor(textColor));

    }

    public void onCreateGroup(View view){
        Intent myIntent=new Intent(getApplicationContext(),GroupInfo.class);
        startActivity(myIntent);
    }

    public void onShowGroup(View view){
        Intent myIntent=new Intent(getApplicationContext(),ShowGroupActivity.class);
        startActivity(myIntent);
    }
}
