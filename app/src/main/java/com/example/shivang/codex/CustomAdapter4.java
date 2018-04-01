package com.example.shivang.codex;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shivang on 13-04-2017.
 */

public class CustomAdapter4 extends ArrayAdapter<String> {

    private final Context context;
    private ShowGroupActivity showGroupActivity;
    String singleGroupName;

    private final  ArrayList<String> groupList;

    public static class ViewHolder{
        TextView group_Name_Text_View;
        Button rename_group_button;
        Button delete_group_button;

    }
    public CustomAdapter4(ShowGroupActivity  context, ArrayList<String> groupList) {
        super(context,R.layout.custom_row4, groupList);
        this.context = context;
        this.groupList=groupList;
        showGroupActivity=context;

    }
    public String getItem(int position) {
        return groupList.get(position);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        SharedPreferences settings = getContext().getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);
        if(convertView==null) {
            LayoutInflater mInflator=LayoutInflater.from(getContext());
            convertView = mInflator.inflate(R.layout.custom_row4, null);
            holder = new CustomAdapter4.ViewHolder();
            holder.group_Name_Text_View = (TextView) convertView.findViewById(R.id.group_Name_Text_View);
            holder.rename_group_button = (Button) convertView.findViewById(R.id.rename_group_button);
            holder.delete_group_button = (Button) convertView.findViewById(R.id.delete_group_button);
            holder.rename_group_button.setBackgroundColor(Color.parseColor(textColor));
            holder.delete_group_button.setBackgroundColor(Color.parseColor(textColor));
            singleGroupName=getItem(position);
            holder.rename_group_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RenameGroupDBox dialogBox=new RenameGroupDBox(showGroupActivity,singleGroupName);
                    dialogBox.show();


                }
            });

            holder.delete_group_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupsHelper obj=new GroupsHelper(getContext());
                    obj.deleteTable(singleGroupName);
                    Intent myIntent=new Intent(context,ShowGroupActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    showGroupActivity.startActivity(myIntent);
                    Log.d("delete buttton clicked","deleted");
                }
            });

            convertView.setTag(holder);
        }
        else{
            holder=null;
            holder=(CustomAdapter4.ViewHolder)convertView.getTag();
        }


          singleGroupName=getItem(position);
        holder.group_Name_Text_View.setText(singleGroupName);






        return convertView;

    }
}
