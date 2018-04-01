package com.example.shivang.codex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shivang on 11-04-2017.
 */
public class CustomAdapter2 extends ArrayAdapter<String> {

    ArrayList<String> groupList;

    public CustomAdapter2(Context context, ArrayList<String> groupList) {
        super(context,R.layout.custom_row,groupList);
        this.groupList=groupList;

    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    public String getItem(int position) {
        return groupList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflator = LayoutInflater.from(getContext());
        View customRow=mInflator.inflate(R.layout.custom_row2,parent,false);
        TextView textView=(TextView)customRow.findViewById(R.id.custom_row2);
        textView.setText(groupList.get(position));
        textView.setTag(groupList.get(position));
        return customRow;

    }
}
