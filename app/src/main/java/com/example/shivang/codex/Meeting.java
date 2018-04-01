package com.example.shivang.codex;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shivang on 21-04-2017.
 */

public class Meeting {

   ArrayList<String> names=new ArrayList<>();



    public String getName(Context context) {
        GroupsHelper obj = new GroupsHelper(context);
        String recName="";
        if(ruid!=null){
            names = obj.getNameFromContactByID(ruid);
            Log.d("sank","null");
            for(String x:names){
                recName=recName+x+" ";
            }
            return recName;
        }
            return null;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public Date getDATE() {
        return DATE;
    }

    public void setDATE(Date DATE) {
        this.DATE = DATE;
    }

    public Date getTIME() {
        return TIME;
    }

    public void setTIME(Date TIME) {
        this.TIME = TIME;
    }

    public ArrayList<Integer> getRuid() {
        return ruid;
    }

    public void setRuid(ArrayList<Integer> ruid) {
        this.ruid = ruid;
    }

    int ID;
    String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String LATITUDE;



    String LONGITUDE;
    String MESSAGE;
    Date DATE;
    Date TIME;
    ArrayList<Integer> ruid;
    int IS_MY;

    public int getIS_MY() {
        return IS_MY;
    }

    public void setIS_MY(int IS_MY) {
        this.IS_MY = IS_MY;
    }
}
