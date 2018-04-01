package com.example.shivang.codex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.shivang.codex.SignINActivity.PREFS_NAME;

public class SelectReceiverGroup extends AppCompatActivity {
    ListView myList;ArrayList<String> groupList;ArrayList<String> returnNameFromGroup;ArrayList<String> markedUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_receiver_group);

        GroupsHelper obj=new GroupsHelper(getBaseContext());
        groupList=obj.getAllContacts();
        Log.e("SelectReceiver",groupList.size()+"");

        myList=(ListView)findViewById(R.id.receiverGrpList);
        ListAdapter sec_Adapter=new CustomAdapter5(this,groupList);
        myList.setAdapter(sec_Adapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Intent myIntent=new Intent(getApplicationContext(),SpecificGrp.class);
                myIntent.putExtra("NameOfGroup",String.valueOf(parent.getItemAtPosition(position)));

                SharedPreferences preferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("currentGroupName",String.valueOf(parent.getItemAtPosition(position)));
                editor.commit();
                startActivity(myIntent);*/
                Log.e("grpReceive","poin0");
                GroupsHelper obj=new GroupsHelper(getBaseContext());
                markedUID=new ArrayList<>();
                Log.e("grpReceive","point1");
                returnNameFromGroup=obj.getNameinArray(String.valueOf(parent.getItemAtPosition(position)));
                Log.d("tsg",returnNameFromGroup.get(0));
                Log.e("grpReceive",returnNameFromGroup.size()+"");
                for(String x:returnNameFromGroup) {
                    markedUID.add(obj.getUID(x));
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("receiverArray",markedUID);
                setResult(6,returnIntent);
                finish();

            }
        });
    }
}
