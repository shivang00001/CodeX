package com.example.shivang.codex;

import android.provider.BaseColumns;

/**
 * Created by shivang on 04-03-2017.
 */

public final class UsersGroup {

    private UsersGroup(){}

    public static class GroupDtbsSchma implements BaseColumns{

        //schema
        public static final String TABLE_NAME="Groups";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_NUMBER = "Number";
        //query to create table
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + GroupDtbsSchma.TABLE_NAME + " (" +
                        GroupDtbsSchma._ID + " INTEGER PRIMARY KEY," +
                        GroupDtbsSchma.COLUMN_NAME_NAME + " TEXT," +
                        GroupDtbsSchma.COLUMN_NAME_NUMBER + " TEXT)";
        //query to delete table
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + GroupDtbsSchma.TABLE_NAME;
    }
}
