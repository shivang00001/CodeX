package com.example.shivang.codex;


import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.shivang.codex.GroupsHelper.CONTACTS_TABLE_NAME;
import static com.example.shivang.codex.R.id.mylist;
import static com.example.shivang.codex.SignINActivity.PREFS_NAME;
import static com.example.shivang.codex.R.id.editText;

public class GroupInfo extends AppCompatActivity {

    ArrayList<Integer> status;
    ArrayList<String> contactArray;
    ArrayList<String> contactNumber;
    static  String databasePath;
    ListView mylist;int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

         contactArray = new ArrayList<>();
         contactNumber = new ArrayList<>();
        status=new ArrayList<>();
        try {
            Log.e("Contact", "1 "+contactArray);
            //Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);


           /* Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null,  "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");*/
            GroupsHelper obj=new GroupsHelper(getBaseContext());
            contactArray=obj.getNameFromContact();
            contactNumber=obj.getNumberFromContact();
           int i=0;
            while (i<contactArray.size()) {
               /* String name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));*/

                /*contactArray.add(name);
                contactNumber.add(phoneNumber);*/
                status.add(i,0);
                i++;
                Log.e("Contact", " "+contactArray.toString());
            }
            //cur.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //sorting of an array
       /* String temporaryString,temporaryNumber;
        for(int i=0;i<contactArray.size()-1;i++){
            for(int j=0;j<(contactArray.size()-i-1);j++){
                if(contactArray.get(j+1).compareTo(contactArray.get(j))<0){
                    temporaryString=contactArray.get(j);
                    contactArray.add(j,contactArray.get(j+1));
                    contactArray.add((j+1),temporaryString);

                    temporaryNumber=contactNumber.get(j);
                    contactNumber.add(j,contactNumber.get(j+1));
                    contactNumber.add((j+1),temporaryNumber);

                }
            }

        }*/

        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_group_info);
        Button button=(Button)findViewById(R.id.groupInfoOk);
        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        button.setBackgroundColor(Color.parseColor(textColor));

        //Collections.sort(contactArray);
        mylist=(ListView)findViewById(R.id.mylist1);
        ListAdapter myAdapter=new CustomAdapter(this,contactArray,status);
        mylist.setAdapter(myAdapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomAdapter.ViewHolder holder=(CustomAdapter.ViewHolder) view.getTag();

                if(status.get(i)==1){
                    holder.checkBox.setChecked(false);
                    status.set(i,0);
                }

                else{
                    holder.checkBox.setChecked(true);
                    status.set(i,1);
                }
            }
        });


    }
    public void onscrollListClick(View view){

        String firstLetter = (String) view.getTag();
        int index = 0;
        if ((mylist !=null)) {
            for (int i=0;i<contactArray.size();i++) {
                String string= contactArray.get(i);
                String string1=string.toString();
                if (string1.toLowerCase().startsWith(firstLetter.toLowerCase())) {
                    index = i;
                    Log.e("GroupInfo",firstLetter+"?"+i+"?"+string1);
                    break;
                }
            }
        }
        mylist.smoothScrollToPositionFromTop(index,0);
    }

    public  void onGrpInfoOk(View view){
        int databaseVersion=1;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        count=0;
        ArrayList<String> markedNames=new ArrayList<>();
        ArrayList<String> markedNumber=new ArrayList<>();
        EditText editText=(EditText)findViewById(R.id.groupNameEditText);
        String groupName=editText.getText().toString();
        for(int j:status){

            if(j==1){
              String name=contactArray.get(count);
                markedNames.add(name);
                String number=contactNumber.get(count);
                markedNumber.add(number);
            }
            count++;
        }
        if(groupName.isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter the name of group",Toast.LENGTH_SHORT).show();
        }
        if(markedNames.isEmpty()||markedNumber.isEmpty()){
            Toast.makeText(getApplicationContext(),"Select someone,please!",Toast.LENGTH_SHORT).show();
        }
        GroupsHelper groupsHelper=new GroupsHelper(getBaseContext(),groupName,databaseVersion,true);

        // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

        editor.commit();

        groupsHelper.createTable(groupName);
        groupsHelper.addEntries(groupName,markedNames,markedNumber);


       /* for(String x:groupsHelper.getNames(groupName)){
            Log.d("GroupsHelper",x);
        }*/
        finish();
    }
}
