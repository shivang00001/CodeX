package com.example.shivang.codex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.shivang.codex.GroupInfo.databasePath;
import static com.example.shivang.codex.SignINActivity.PREFS_NAME;

public class ShowGroupActivity extends AppCompatActivity {
    ListView myList;ArrayList<String> groupList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group);

        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_show_group);
        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        /*Button b1=(Button)findViewById(R.id.rename_group_button);
        Button b2=(Button)findViewById(R.id.delete_group_button);*/

        GroupsHelper obj=new GroupsHelper(getBaseContext());
        groupList=obj.getAllContacts();


        myList=(ListView)findViewById(R.id.myList2);
        ListAdapter sec_Adapter=new CustomAdapter4(this,groupList);
        myList.setAdapter(sec_Adapter);

//        b1.setBackgroundColor(Color.parseColor(textColor));
   //     b2.setBackgroundColor(Color.parseColor(textColor));

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent=new Intent(getApplicationContext(),SpecificGrp.class);
                myIntent.putExtra("NameOfGroup",String.valueOf(parent.getItemAtPosition(position)));
                SharedPreferences preferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("currentGroupName",String.valueOf(parent.getItemAtPosition(position)));
                editor.commit();
                startActivity(myIntent);
            }
        });
    }

    public void onGroupMeth(View view){
        Intent myIntent=new Intent(getApplicationContext(),SpecificGrp.class);
        //myIntent.putExtra("NameOfGroup",String.valueOf(parent.getItemAtPosition(position)));

        SharedPreferences preferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("currentGroupName",String.valueOf(((TextView)view).getText().toString()));
        editor.commit();
        startActivity(myIntent);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    /*public void onRenameGroup(View view){
    RenameGroupDBox dialogBox=new RenameGroupDBox(ShowGroupActivity.this);
        dialogBox.show();
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String  newGName = settings.getString("newGroupName",null);
        GroupsHelper obj=new GroupsHelper(getBaseContext());
       // obj.changeTableName(oldName,newGName);
        //Toast.makeText(getApplicationContext(),"Name Changed",Toast.LENGTH_SHORT).show();

    }*/



}
