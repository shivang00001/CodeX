package com.example.shivang.codex;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends ArrayAdapter<String> {


    private final Context context;
    private final ArrayList<String> contactArray;
    ViewHolder holder;
    ListContactClass listContactClass;

   public static class ViewHolder{
         TextView contactTextView;
          CheckBox checkBox;
    }

    ArrayList<Integer> status;
    public CustomAdapter(Context context, ArrayList<String> contactArray, ArrayList<Integer> status) {
        super(context,R.layout.custom_row, contactArray);
        this.context = context;
        this.contactArray = contactArray;
        this.status=status;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return contactArray.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflator=LayoutInflater.from(getContext());

        if(convertView==null) {
            convertView = mInflator.inflate(R.layout.custom_row, null);
            holder = new ViewHolder();
            holder.contactTextView = (TextView) convertView.findViewById(R.id.contactTextView);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }
        else{
            holder=null;
            holder=(ViewHolder)convertView.getTag();
        }
        if(status.get(position)==1)
            holder.checkBox.setChecked(true);
        else
            holder.checkBox.setChecked(false);

        String singleContact=getItem(position);
        holder.contactTextView.setText(singleContact);
        return convertView;

    }
}
