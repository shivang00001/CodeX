package com.example.shivang.codex;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by shivang on 19-04-2017.
 */
public class CustomAdapterForMeet extends ArrayAdapter<String> {

    ArrayList<String> nameList=new ArrayList<>(),dateList=new ArrayList<>();Context context;
    public CustomAdapterForMeet(Context context, ArrayList<String> numberList,ArrayList<String> dateList) {
        super(context,R.layout.custom_row,dateList);
        this.nameList=numberList;
        this.dateList=dateList;
        this.context=context;
    }

    public int getCount1() {
        return nameList.size();
    }
    public int getCount2() {
        return dateList.size();
    }

    public String getItem1(int position) {
        return nameList.get(position);
    }
    public String getItem2(int position) {
        return dateList.get(position);
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflator = LayoutInflater.from(getContext());
        View customRow=mInflator.inflate(R.layout.custom_row_meet,parent,false);


        TextView textView1=(TextView)customRow.findViewById(R.id.upcomingMeets_tv1);
        TextView textView2=(TextView)customRow.findViewById(R.id.upcomingMeets_tv2);

        textView1.setText(nameList.get(position));
        textView2.setText(dateList.get(position));

        return customRow;

    }



}
