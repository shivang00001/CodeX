package com.example.shivang.codex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.shivang.codex.GroupsHelper.MEETING_TABLE_NAME;
import static com.example.shivang.codex.SignINActivity.PREFS_NAME;

public class UpcomingMeetsActivity extends AppCompatActivity {
    private static final String TAG ="UpcomingMeetsActivity" ;
    TextView textView;
        ListView meetList;
        ArrayList<Meeting> allMeet=new ArrayList<>();
    /*ArrayList<Integer> UID=new ArrayList<>();
    ArrayList<String> UID1=new ArrayList<>();ArrayList<String> namelist=new ArrayList<>();
    ;ArrayList<Integer> rid=new ArrayList<>();
    private ArrayList<Integer> receiveID=new ArrayList<>();*/

    ArrayList<Integer> MID=new ArrayList<>();
    ArrayList<String> nameList=new ArrayList<>();
    CustomAdapterForMeet adapter;
    ArrayList<Date>dateList1=new ArrayList<>();
    ArrayList<String> dateList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_meets);
        Date start=null;
        GroupsHelper grpHelper=new GroupsHelper(this);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = (df.format(Calendar.getInstance().getTime()));

        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_upcoming_meets);
        Button button=(Button)findViewById(R.id.upcomingMeetsCloseButton);
        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        button.setBackgroundColor(Color.parseColor(textColor));
        try {
             start = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);

        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
        MeetDataBaseHelper obj= new MeetDataBaseHelper(this);
        Log.e("start: ",start.toString());

        /*allMeet=obj.getAllMeet(start);
        Log.e("AllMeetSze",allMeet.size()+"");
        ArrayList<String> ruid=new ArrayList<>();
        for(Meeting m:allMeet){
            Log.e("meetProp",m.getID()+"");
             //rid=m.getID();
            rid.add(m.getID());
            receiveID=m.getRuid();

            namelist=m.getName();
            for(String x:namelist){
                nameList.add(x);
            }
            Log.d("sank",allMeet.size()+"");
            Log.d("sank",rid.toString()+"");
            for(Integer i:rid){
                ruid.add(String.valueOf(i));
            }
            dateList.add(m.getDATE().toString());
        }

        for(int i:UID){
            UID1.add(String.valueOf(i));
        }*/
        allMeet=obj.getAllMeet(start);
        for(Meeting m:allMeet){
            MID.add(m.getID());
            nameList.add(m.getName(this));
            dateList1.add(m.getDATE());

            Log.e("checcErroe",m.getDATE().toString());
        }
        Log.e("checcErroe",dateList1.toString());
        for(Date d:dateList1){
            dateList.add(d.toString());
        }




        meetList=(ListView)findViewById(R.id.upcomingMeetsList);
        Log.e(TAG,nameList.toString());
        adapter=new CustomAdapterForMeet(getBaseContext(),nameList,dateList);
        meetList.setAdapter(adapter);
                meetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent=new Intent(getApplicationContext(),SpecificMeet.class);
                myIntent.putExtra("Name",String.valueOf(adapter.getItem1(position)));
                myIntent.putExtra("Date",String.valueOf(adapter.getItem2(position)));
                myIntent.putExtra("MID",MID.get(position));
                startActivity(myIntent);
            }
        });


        /*Cursor cursor;
        GroupsHelper obj = new GroupsHelper(getBaseContext());
        SQLiteDatabase db = obj.getWritableDatabase();
        String selectQuery = "SELECT  DATE,TIME FROM " + MEETING_TABLE_NAME;
        cursor = db.rawQuery(selectQuery, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            textView.setText("Address: "+cursor.getString(cursor.getColumnIndex("ADDRESS"))+"\n"+
                    "Message: "+cursor.getString(cursor.getColumnIndex("MESSAGE"))+"\n"+
                    "Date: "+cursor.getString(cursor.getColumnIndex("DATE"))+"\n"+
                    "Time: "+cursor.getString(cursor.getColumnIndex("DATE"))+"\n"+"\n"+"\n");


        }*/

    }
    public void onUpcomingMeetClose(View view){
        Intent myIntent=new Intent(UpcomingMeetsActivity.this,FuncActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
    }
}
