package com.example.shivang.codex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        textView=(TextView)findViewById(R.id.textView7);
        Bundle extras=getIntent().getExtras();
        textView.setText("Address: "+extras.getString("address")+"\n"+
                         "Latitude: "+extras.getDouble("latitude")+"\n"+
                         "Longitude: "+extras.getDouble("longitude")+"\n"+
                         "Message: "+getIntent().getStringExtra("message")+"\n"+
                         "Date: "+extras.getInt("day")+"/"+extras.getInt("month")+"/"+extras.getInt("year")+"\n"+
                         "Time: "+extras.getInt("hour")+"hrs "+extras.getInt("min")+"min"
        );
    }


}
