package com.example.shivang.codex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static com.example.shivang.codex.UsersGroup.GroupDtbsSchma.COLUMN_NAME_NAME;

/**
 * Created by shivang on 04-03-2017.
 */

public class GroupsHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "MyGroup.db";

    public static  String TABLE_NAME;
    public static  String GROUP_TABLE_NAME ="group_table";
    public static  String MEETING_TABLE_NAME ="MY_MEETS";
    public static  String MEMOS_TABLE_NAME ="MEMOS_OF_MEETS";
    public static  boolean isCreate =true;
    public static String CONTACTS_TABLE_NAME="contact_table";

    public GroupsHelper(Context context,String table,int DATABASE_VERSION,boolean create) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME=table;
        isCreate=create;
    }
    public GroupsHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+GROUP_TABLE_NAME+" ( " +
                "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Group_Name"+ " TEXT)" );

        db.execSQL("CREATE TABLE "+MEETING_TABLE_NAME+" (MID INTEGER PRIMARY KEY,RECEIVER_UID TEXT,ADDRESS TEXT,DATE TEXT,TIME TEXT,MESSAGE TEXT,IS_MY INTEGER)");

        db.execSQL("CREATE TABLE "+MEMOS_TABLE_NAME+" ( ID INTEGER REFERENCES MY_MEETS,MEMOS TEXT )");

        db.execSQL("CREATE TABLE "+CONTACTS_TABLE_NAME+" (ID INTEGER PRIMARY KEY,NAME TEXT,NUMBER TEXT)");
    }
    public void createTable(String TABLE_NAME){
        SQLiteDatabase db=getWritableDatabase();
        try{
        db.execSQL(

                "CREATE TABLE " + TABLE_NAME + " (" +
                        UsersGroup.GroupDtbsSchma._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME_NAME + " TEXT," +
                        UsersGroup.GroupDtbsSchma.COLUMN_NAME_NUMBER + " TEXT)");
        ContentValues cv = new ContentValues();
        cv.put("Group_Name",TABLE_NAME);
        long a=db.insert(GROUP_TABLE_NAME,null,cv);
        if(a!=-1)Log.i("Inserted","Success");}
        catch (Exception e){
            Log.e("grouphelper",e.toString());
        }
        finally {
            db.close();
        }

    }
    public void addEntries(String name ,ArrayList<String> names,ArrayList<String> mobile) {
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
        try{

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
        db.close();}
        catch (Exception e){
            Log.e("group",e.toString());
        }

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

    public ArrayList<String> addPersonTOSend(ArrayList<String> markedUID, ArrayList<String> markedNames) {

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

    public ArrayList<String> getNameinArray( String s) {

        SQLiteDatabase db = getWritableDatabase();

        ArrayList<String> returnGroupItems=new ArrayList<>();

            String selectQuery = "SELECT  NAME FROM " + s;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    returnGroupItems.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }

        return returnGroupItems;
    }

    public String getUID(String x) {
        String UID="";
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT  ID FROM " + CONTACTS_TABLE_NAME+" WHERE NAME='"+x+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int uid=(cursor.getInt(0));
                UID=String.valueOf(uid);

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
        ArrayList<Integer>idFromMeetTable=new ArrayList<>();
        ArrayList<String>nameFromMeetTable=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query =  "SELECT RECEIVER_UID FROM " + MEETING_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){

                String id=cursor.getString(0);
                String[] ruid=id.split(" ");
                for(String x:ruid) {
                    idFromMeetTable.add(Integer.parseInt(x));
                }


        }
        nameFromMeetTable=getNameFromContactByID(idFromMeetTable);
        db.close();

        return nameFromMeetTable;
    }

    public ArrayList<String> getNameFromContactByID(ArrayList<Integer> idFromMeetTable) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String>nameFromMeetTable=new ArrayList<>();
        Log.d("checkID",""+idFromMeetTable.size());
        Log.d("checkID",""+idFromMeetTable.toString());
        for(int id:idFromMeetTable){


                String query =  "SELECT  NAME FROM " +CONTACTS_TABLE_NAME+" WHERE ID= '"+id+"'";
                Log.e("checkID",id+"");
                Cursor cursor = db.rawQuery(query, null);
                while (cursor.moveToNext()){

                    String name=cursor.getString(0);
                    nameFromMeetTable.add(name);



                }
            Log.e("sank",nameFromMeetTable.toString());
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
            String[] ruid=id.split(" ");
            for(String x:ruid) {
                idFromMeetTable.add(x);
            }
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

    public ArrayList<String> getMeetContents(int meetID) {
        ArrayList<String> meetContents = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + MEETING_TABLE_NAME+" WHERE MID='"+ meetID+"'";

        SQLiteDatabase db =this. getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                for(int i=0;i<9;i++) {
                    Log.e("detailsOFMEtt", String.valueOf(cursor.getString(i)));
                    meetContents.add(String.valueOf(cursor.getString(i)));
                }
            } while (cursor.moveToNext());
        }

        // return contact list
        db.close();

        return meetContents;

    }

    public ArrayList<String> getMeetUSingNameDAte(String UID, String date) {
        ArrayList<String> meetContents = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + MEETING_TABLE_NAME+" WHERE RECEIVER_UID LIKE '%"+ UID+"%' AND DATE='"+date+"'";
        String selectQuery = "SELECT  * FROM " + MEETING_TABLE_NAME;
        Log.e("GrpHelper",selectQuery);
        SQLiteDatabase db =this. getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                for(int i=0;i<9;i++){
                    meetContents.add(cursor.getString(i));
                Log.e("GrpHelper",cursor.getString(i));}

            } while (cursor.moveToNext());
        }

        // return contact list
        db.close();
        return meetContents;
    }
}
