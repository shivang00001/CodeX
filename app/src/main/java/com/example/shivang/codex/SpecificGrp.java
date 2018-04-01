package com.example.shivang.codex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.shivang.codex.SignINActivity.PREFS_NAME;

public class SpecificGrp extends AppCompatActivity {
     ListView list3;ArrayList<String> numberList;String tableName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_grp);
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);

        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_specific_grp);
        Button b=(Button)findViewById(R.id.addPersonButton);
        //Button b2=(Button)findViewById(R.id.deleteButton);
        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        b.setBackgroundColor(Color.parseColor(textColor));

        tableName= settings.getString("currentGroupName",null);
        GroupsHelper obj=new GroupsHelper(getBaseContext());

        numberList=obj.getAllElementsOfGrp(tableName);

        list3=(ListView)findViewById(R.id.list3);
        ListAdapter adapter3=new CustomAdapter3(this,numberList);
        list3.setAdapter(adapter3);
        //b2.setBackgroundColor(Color.parseColor(textColor));
        list3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String persn2Del=String.valueOf(parent.getItemAtPosition(position));
                GroupsHelper obj=new GroupsHelper(getBaseContext());
                obj.deleteEntry(persn2Del,tableName);
                Toast.makeText(getApplicationContext(),"Person Deleted",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
    public void onAddPerson(View view){
        Intent intent=new Intent(getApplicationContext(),AddPersonActivity.class);
        startActivity(intent);
    }


   /* public void onDeleteEntry(View view){
        Button deleteButton=(Button)findViewById(R.id.deleteButton);
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        tableName= settings.getString("currentGroupName",null);
        GroupsHelper obj=new GroupsHelper(getBaseContext());
        obj.deleteEntry(String entry,tableName);
        Toast.makeText(getApplicationContext(),"person deleted",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),ShowGroupActivity.class);
                startActivity(intent);

    }*/


}
