package com.example.shivang.codex;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shivang on 11-04-2017.
 */
public class CustomAdapter3 extends ArrayAdapter<String> {
    ArrayList<String> numberList;Button deleteButton;
    public CustomAdapter3(Context context, ArrayList<String> numberList) {
        super(context,R.layout.custom_row,numberList);
        this.numberList=numberList;
        Log.d("GroupsHelper_Inserted",""+numberList.size());
    }
    public int getCount() {
        Log.d("GroupsHelper_Inserted",""+numberList.size());
        return numberList.size();
    }

    public String getItem(int position) {
        return numberList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences settings =getContext().getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        LayoutInflater mInflator = LayoutInflater.from(getContext());
        View customRow=mInflator.inflate(R.layout.custom_row3,parent,false);

        Button deleteButton=(Button)customRow.findViewById(R.id.deleteButton);
        TextView textView=(TextView)customRow.findViewById(R.id.custom_row3);

        textView.setText(numberList.get(position));
        deleteButton.setBackgroundColor(Color.parseColor(textColor));
        return customRow;

    }



}
