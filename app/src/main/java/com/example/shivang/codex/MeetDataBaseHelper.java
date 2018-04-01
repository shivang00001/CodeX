package com.example.shivang.codex;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.shivang.codex.UsersGroup.GroupDtbsSchma.COLUMN_NAME_NAME;


/**
 * Created by shivang on 21-04-2017.
 */

public class MeetDataBaseHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "MyGroups.db";
        private String TAG="MeetDataBaseHelper";
        public static  String TABLE_NAME;
        public static  String GROUP_TABLE_NAME ="group_table";
        public static  String MEETING_TABLE_NAME ="MY_MEETS";
        public static  String MEMOS_TABLE_NAME ="MEMOS_OF_MEETS";
        public static  boolean isCreate =true;
        public static String CONTACTS_TABLE_NAME="contact_table";


        public MeetDataBaseHelper(Context context, String table, int DATABASE_VERSION, boolean create) {
            super(context, DATABASE_NAME, null, 2);
            TABLE_NAME=table;
            isCreate=create;
        }

        public MeetDataBaseHelper(Context context){
            super(context,DATABASE_NAME,null,1);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE "+MEETING_TABLE_NAME+" (MID INTEGER PRIMARY KEY," +
                    "RECEIVER_UID TEXT," +
                    "LATITUDE TEXT," +
                    "LONGITUDE TEXT," +
                    "ADDRESS TEXT," +
                    "DATE TEXT," +
                    "TIME TEXT," +
                    "MESSAGE TEXT," +
                    "IS_MY INTEGER)"
            );

        }

        public void print(String tablename){
            SQLiteDatabase db=getWritableDatabase();
            String query="SELECT * FROM "+tablename;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Log.e(TAG,cursor.getInt(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" "+cursor.getString(4)+" "+cursor.getString(5)+" " +cursor.getInt(6));
                } while (cursor.moveToNext());
            }

            // return contact list
            db.close();

    }


        @Override
         public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //add group here
        Log.d("GroupsHelper","OnUpdate");
        String SQL_CREATE_ENTRIES;
        if(isCreate) {
            SQL_CREATE_ENTRIES =
                    "CREATE TABLE " + TABLE_NAME + " (" +
                            UsersGroup.GroupDtbsSchma._ID + " INTEGER PRIMARY KEY," +
                            COLUMN_NAME_NAME + " TEXT," +
                            UsersGroup.GroupDtbsSchma.COLUMN_NAME_NUMBER + " TEXT)";
        }
        else {
            SQL_CREATE_ENTRIES =
                    "DROP TABLE " + TABLE_NAME ;
        }
        db.execSQL(SQL_CREATE_ENTRIES);
    }



        /* function: add a meeting object to database
           parameters: Meeting obj
           return type:void
         */
        public void addMeet(Meeting m){
            SQLiteDatabase db=getWritableDatabase();
            int id=m.getID();
            String lat=m.getLATITUDE();
            String lng=m.getLONGITUDE();
            String msg=m.getMESSAGE();
            Date date=m.getDATE();
            String m_date=(date.getYear()+1900)+"-"+(date.getMonth()+1)+"-"+date.getDate();
            Date time=m.getTIME();
            String m_time=time.getHours()+":"+time.getMinutes();
            ArrayList<Integer> ruid=m.getRuid();
            String x="";
            for(int s:ruid){
                x += s+" ";
            }
            String address=m.getAddress();

            ContentValues cv = new ContentValues();
            cv.put("MID",id);
            cv.put("RECEIVER_UID",x);
            cv.put("LATITUDE",lat);
            cv.put("LONGITUDE",lng);
            cv.put("ADDRESS",address);
            cv.put("DATE",m_date);
            //Log.d("sa")
            cv.put("TIME",m_time);
            cv.put("MESSAGE",msg);
            long i=db.insert(MEETING_TABLE_NAME, null, cv);
            Log.d("GroupMeetHelper",i+"");
            db.close();

        }



    public void deleteEntryFromMeets(int mid) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + MEETING_TABLE_NAME+ " WHERE MID='"+mid+"'");
            Log.d("DeletePerson","person deleted "+mid);
            db.close();
            //db.delete(tableName, COLUMN_NAME_NAME + "=" + entry, entry);
        }
        catch (Exception e){
            db.close();
            Log.d("DeletePerson",e.toString());
        }
    }


        /* function: get meeting based on id passed
           parameters: Integer id of meeting
           return type:Meeting obj
         */
        public Meeting getMeet(int id){
            Meeting obj=new Meeting();
            ArrayList<Integer> r_id=new ArrayList<>();
            Date date1=null; Date time1=null;
            String selectQuery = "SELECT  * FROM " + MEETING_TABLE_NAME+" WHERE MID='"+ id+"'";
            SQLiteDatabase db =this. getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                            obj.setID(Integer.parseInt(cursor.getString(0)));
                            String ruid=cursor.getString(1);
                            String [] rec_id=ruid.split(" ");
                            for(String x:rec_id){
                               r_id.add(Integer.parseInt(x));
                            }
                            obj.setRuid(r_id);
                            obj.setLATITUDE(cursor.getString(2));
                            obj.setLONGITUDE(cursor.getString(3));
                            obj.setAddress(cursor.getString(4));
                            String date=cursor.getString(5);
                            try {
                                 date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                            obj.setDATE(date1);

                            String time=cursor.getString(6);
                            Log.e("time",time);
                            try {
                                time1 = new SimpleDateFormat("HH:mm").parse(time);
                                Log.e("time1",time1+"");
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                            obj.setTIME(time1);
                            obj.setMESSAGE(cursor.getString(7));
                            obj.setIS_MY(cursor.getInt(8));
                            } while (cursor.moveToNext());
                        }

                // return contact list
                db.close();
                return obj;

        }


        /* function: get all meeting on or after date passed
           parameters: Date
           return type: ArrayList<Meeting>
         */
        public ArrayList<Meeting> getAllMeet(Date date){
            Log.e(TAG,date.toString());
            ArrayList<Meeting> allMeet=new ArrayList<>();
            Date d1=null;
            String selectQuery = "SELECT  * FROM " + MEETING_TABLE_NAME;
            SQLiteDatabase db =this. getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Meeting obj=new Meeting();
                    String d=cursor.getString(5);
                    Log.e("d12:",d.toString());
                    try {
                        d1 = new SimpleDateFormat("yyyy-MM-dd").parse(d);
                        Log.e("d1:",d1.toString());
                    }
                    catch (Exception e){
                        Log.e(TAG,e.toString());
                    }
                    Log.e(TAG,d1.toString());
                    Log.e(TAG,date.toString());
                    if(d1.after(date)||d1.equals(date)){
                        Log.e(TAG,d1.toString());Log.e(TAG,date.toString());
                        obj.setID(cursor.getInt(0));
                        String rID=cursor.getString(1);
                        String[] recID=rID.split(" ");
                        Log.d("sank1",rID);

                        ArrayList<Integer> arrayList=new ArrayList<>();
                        for(String x:recID){
                            arrayList.add(Integer.parseInt(x));
                            Log.d("sank2",x);
                        }
                        Log.d("sank3",rID);
                        obj.setRuid(arrayList);
                        obj.setAddress(cursor.getString(4));
                        Date dat1=null;
                        try {
                             Date dat = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(5));
                                    dat1=dat;
                        }catch (Exception e){
                            Log.e(TAG,e.toString());
                        }
                        obj.setDATE(dat1);
                        Date tim1=null;
                        try{
                            Date tim=new SimpleDateFormat("HH:mm").parse(cursor.getString(6));
                            tim1=tim;
                        }catch (Exception e){Log.e(TAG,e.toString());}
                        obj.setTIME(tim1);
                        obj.setMESSAGE(cursor.getString(7));
                        obj.setIS_MY(cursor.getInt(8));
                        allMeet.add(obj);
                    }else{Log.e(TAG,"no meet");}

                    Log.e("MeetDHelper","point reached");
                } while (cursor.moveToNext());
            }

            return allMeet;
        }
        public void delete(int id){

        /* function: delete a meeting from database based on id passed
           parameters: Integer id
           return type: void
         */

            SQLiteDatabase db = this.getWritableDatabase();
            try {
                Log.e("meetGrpHelper","point reached");
                db.execSQL("DELETE FROM " + MEETING_TABLE_NAME+ " WHERE ID="+id);

                db.close();

            }
            catch (Exception e){
                db.close();
                Log.d(TAG,e.toString());
            }
        }







       /*public void addEntries(String name , ArrayList<String> names, ArrayList<String> mobile) {
            SQLiteDatabase db;
            //start of for loop
            for( int a=0;a<names.size();a++) {
                db=getWritableDatabase();
                ContentValues cv = new ContentValues();
                Log.d("GroupsHelper_Inserted", names.get(a) + "/" + mobile.get(a));
                cv.put(COLUMN_NAME_NAME, names.get(a));
                cv.put(UsersGroup.GroupDtbsSchma.COLUMN_NAME_NUMBER, mobile.get(a));
                long i=db.insert(name, null, cv);
                Log.d("GroupsHelper_Inserted",""+i);
                db.close();
            }




            Log.d("GroupsHelper","Entries added");
        }

        public void addEntries2MeetTable(int ID,String receiverID,String address,String meet_date,String meet_time,String msg,int isMY){
            SQLiteDatabase db=getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("MID",ID);
            cv.put("RECEIVER_UID",receiverID);
            cv.put("ADDRESS",address);
            cv.put("DATE",meet_date);
            cv.put("TIME",meet_time);
            cv.put("MESSAGE",msg);
            long i=db.insert(MEETING_TABLE_NAME, null, cv);
            Log.d("Message Inserted","success"+i);
            db.close();
        }

        public void addEntries2MemoTable(String memo ){
            SQLiteDatabase db=getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("MEMOS",memo);
            db.insert(MEMOS_TABLE_NAME, null, cv);
            db.close();
        }
        //getAllContacts()
        // Getting All Contacts
        public  ArrayList<String> getAllContacts() {
            ArrayList<String> contactList = new ArrayList<String>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + GROUP_TABLE_NAME;

            SQLiteDatabase db =getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    contactList.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }

            // return contact list
            db.close();
            return contactList;
        }
        public ArrayList<String>  getNameFromContact(){
            ArrayList<String> contactList = new ArrayList<>();
            String selectQuery = "SELECT  NAME FROM " + CONTACTS_TABLE_NAME;
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()&&cursor.getCount()>0) {
                do {
                    contactList.add(cursor.getString(0));

                    Log.d("NAME_GH",cursor.getString(0));
                } while (cursor.moveToNext());
            }

            // return contact list
            db.close();
            return contactList;

        }
        public ArrayList<String>  getNumberFromContact(){
            ArrayList<String> contactList = new ArrayList<String>();
            String selectQuery = "SELECT  NUMBER FROM " + CONTACTS_TABLE_NAME;
            SQLiteDatabase db =this. getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    contactList.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            // return contact list
            db.close();
            return contactList;

        }

        public  ArrayList<String> getAllElementsOfGrp(String tableName) {
            ArrayList<String> contactList = new ArrayList<String>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + tableName;

            SQLiteDatabase db =this. getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    contactList.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }

            // return contact list
            db.close();
            return contactList;
        }
        public ArrayList<String> getNames(String table){
            ArrayList<String> names=new ArrayList<>();

            String selectQuery = "SELECT  * FROM " + table;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    names.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            db.close();
            return names;

        }

        public void deleteEntry(String entry,String tableName){
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                db.execSQL("DELETE FROM " + tableName+ " WHERE "+COLUMN_NAME_NAME+"='"+entry+"'");
                db.close();
                //db.delete(tableName, COLUMN_NAME_NAME + "=" + entry, entry);
            }
            catch (Exception e){
                db.close();
                Log.d("DeletePerson","error caught");
            }
        }

        public void addPerson(String tableName,ArrayList<String> names,ArrayList<String> mobile) {
            SQLiteDatabase db = getWritableDatabase();
            //start of for loop
            for (int a = 0; a < names.size(); a++) {
                ContentValues cv = new ContentValues();
                Log.d("GroupsHelper_Inserted", names.get(a) + "/" + mobile.get(a));
                cv.put(COLUMN_NAME_NAME, names.get(a));
                cv.put(UsersGroup.GroupDtbsSchma.COLUMN_NAME_NUMBER, mobile.get(a));
                try{db.insert(tableName, null, cv);}catch (Exception e){}
            }

            db.close();
        }

        public void addPersonToContact(String uid,String name,String number) {
            SQLiteDatabase db = getWritableDatabase();
            //start of for loop

            ContentValues cv = new ContentValues();
            cv.put("ID", uid);
            cv.put("NAME", name);
            cv.put("NUMBER", number);
            try{db.insert(CONTACTS_TABLE_NAME, null, cv);}catch (Exception e){db.close();}
            db.close();

        }
        public  void changeTableName(String oldName,String newName){
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("ALTER TABLE " + oldName + " RENAME TO " + newName);
            db.execSQL("UPDATE group_table SET Group_Name = '"+newName+"' WHERE Group_Name = '"+ oldName+"'");
            db.close();
        }

        public void deleteTable(String tableName){
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS "+tableName);
            db.delete(GROUP_TABLE_NAME, "Group_Name" + "= '" + tableName+"'", null);
            db.close();
        }*/





       /* public ArrayList<String> addPersonTOSend(ArrayList<String> markedUID, ArrayList<String> markedNames) {

            SQLiteDatabase db = getWritableDatabase();


            for(String j:markedNames) {
                String name="'"+j+"'";
                String selectQuery = "SELECT  ID FROM " + CONTACTS_TABLE_NAME + " WHERE NAME="+name;
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        Log.d("GroupHelperCheck",cursor.getString(0));
                        markedUID.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
            }
            return markedUID;

        }

        public ArrayList<String> getNameinArray(ArrayList<String> returnGroupItems, String s) {

            SQLiteDatabase db = getWritableDatabase();



            String selectQuery = "SELECT  NAME FROM " + s;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    returnGroupItems.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }

            return returnGroupItems;
        }

        public ArrayList<String> getUID() {
            ArrayList<String> UID=new ArrayList<>();
            SQLiteDatabase db = getWritableDatabase();
            String selectQuery = "SELECT  NAME FROM " + CONTACTS_TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    UID.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }
            return UID;
        }

        public String getSenderName(String senderID) {
            SQLiteDatabase db = getWritableDatabase();
            String senderName="";
            String query =  "SELECT  ID,NAME FROM " + CONTACTS_TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()){
                if(cursor.getInt(0)==Integer.parseInt(senderID)){
                    senderName=cursor.getString(1);
                }

            }
            db.close();
            return senderName;
        }

        public ArrayList<String> getnameFromMeetTable() {
            ArrayList<String>idFromMeetTable=new ArrayList<>();
            ArrayList<String>nameFromMeetTable=new ArrayList<>();
            SQLiteDatabase db = getWritableDatabase();
            String query =  "SELECT RECEIVER_UID FROM " + MEETING_TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()){

                String id=cursor.getString(0);
                idFromMeetTable.add(id);


            }
            nameFromMeetTable=getNameFromContactByID(idFromMeetTable);
            db.close();

            return nameFromMeetTable;
        }

        private ArrayList<String> getNameFromContactByID(ArrayList<String> idFromMeetTable) {
            SQLiteDatabase db = getWritableDatabase();
            ArrayList<String>nameFromMeetTable=new ArrayList<>();
            for(String id:idFromMeetTable){
                String a[]=id.split(" ");
                for(String s:a){
                    String query =  "SELECT  NAME FROM " + CONTACTS_TABLE_NAME+" WHERE ID= "+Integer.parseInt(s);
                    Cursor cursor = db.rawQuery(query, null);
                    while (cursor.moveToNext()){

                        String name=cursor.getString(0);
                        nameFromMeetTable.add(name);


                    }
                }

            }
            return nameFromMeetTable;
        }

        public ArrayList<String> getDatefromMeetTable() {
            ArrayList<String>idFromMeetTable=new ArrayList<>();
            ArrayList<String>dateFromMeetTable=new ArrayList<>();
            SQLiteDatabase db = getWritableDatabase();
            String query =  "SELECT  RECEIVER_UID FROM " + MEETING_TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()){

                String id=cursor.getString(0);
                idFromMeetTable.add(id);


            }
            dateFromMeetTable=getDateFromContactByID(idFromMeetTable);
            db.close();


            return dateFromMeetTable;
        }

        private ArrayList<String> getDateFromContactByID(ArrayList<String> idFromMeetTable) {
            SQLiteDatabase db = getWritableDatabase();
            ArrayList<String>dateFromMeetTable=new ArrayList<>();
            for(String id:idFromMeetTable){
                String query =  "SELECT  DATE FROM " + MEETING_TABLE_NAME+" WHERE RECEIVER_UID= "+id;
                Cursor cursor = db.rawQuery(query, null);
                while (cursor.moveToNext()){

                    String name=cursor.getString(0);
                    dateFromMeetTable.add(name);


                }
            }
            return dateFromMeetTable;
        }

        public String getSenderNameFromMeet(int mid) {
            SQLiteDatabase db = getWritableDatabase();
            String senderName="";
            int senderID=0;
            String query =  "SELECT  ID,SENDER_ID FROM " + CONTACTS_TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()){
                if(cursor.getInt(0)==mid){
                    senderID=cursor.getInt(1);
                }
                senderName=getSenderName(String.valueOf(senderID));

            }
            db.close();
            return senderName;
        }

        public void deleteEntryFromMeets(int mid) {
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                db.execSQL("DELETE FROM " + MEETING_TABLE_NAME+ " WHERE ID="+mid);
                db.close();
                //db.delete(tableName, COLUMN_NAME_NAME + "=" + entry, entry);
            }
            catch (Exception e){
                db.close();
                Log.d("DeletePerson","error caught");
            }
        }

        public ArrayList<String> getMeetContents(int meetID) {
            ArrayList<String> meetContents = new ArrayList<String>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + MEETING_TABLE_NAME+" WHERE ID="+ meetID;

            SQLiteDatabase db =this. getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    for(int i=2;i<6;i++)
                        meetContents.add(cursor.getString(i));
                } while (cursor.moveToNext());
            }

            // return contact list
            db.close();
            return meetContents;

        }

        public ArrayList<String> getMeetUSingNameDAte(String name, String date) {
            ArrayList<String> meetContents = new ArrayList<String>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + MEETING_TABLE_NAME+" WHERE NAME='"+ name+"' AND DATE='"+date+"'";

            SQLiteDatabase db =this. getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    for(int i=0;i<6;i++)
                        meetContents.add(cursor.getString(i));
                } while (cursor.moveToNext());
            }

            // return contact list
            db.close();
            return meetContents;
        }*/
    }


