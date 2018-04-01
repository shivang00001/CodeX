package com.example.shivang.codex;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddPersonActivity extends AppCompatActivity {
    ArrayList<Integer> status;
    ArrayList<String> contactArray;
    ArrayList<String> contactNumber;
    static  String databasePath;
    ListView mylist;int count=0;String tableName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_group_info);
        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        EditText editText=(EditText)findViewById(R.id.groupNameEditText);
        Button button=(Button)findViewById(R.id.groupInfoOk);
        button.setBackgroundColor(Color.parseColor(textColor));
        editText.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(lp);
        GroupsHelper obj=new GroupsHelper(getBaseContext());
        contactArray = new ArrayList<>();
        contactNumber = new ArrayList<>();
        status=new ArrayList<>();
        try {
            Log.e("Contact", "1 "+contactArray);
            //Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
           /* ContentResolver cr = getContentResolver();

            Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null,  "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");*/
            contactArray=obj.getNameFromContact();
            contactNumber=obj.getNumberFromContact();
            int i=0;
            while (i<contactArray.size()) {
                /*String name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));*/

                /*contactArray=obj.getNameFromContact();
                contactNumber=obj.getNumberFromContact();*/
                status.add(i,0);
                i++;
                Log.e("Contact", " "+contactArray);
            }
            //cur.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> markedNames=new ArrayList<>();
                ArrayList<String> markedNumber=new ArrayList<>();
                SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
                 tableName= settings.getString("currentGroupName",null);
                for(int j:status){

                    if(j==1){
                        String name=contactArray.get(count);
                        markedNames.add(name);
                        String number=contactNumber.get(count);
                        markedNumber.add(number);
                    }
                    count++;
                }
                GroupsHelper obj=new GroupsHelper(getBaseContext());
                obj.addPerson(tableName,markedNames,markedNumber);
                Toast.makeText(getApplicationContext(),"person added",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),ShowGroupActivity.class);
                startActivity(intent);

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

}
