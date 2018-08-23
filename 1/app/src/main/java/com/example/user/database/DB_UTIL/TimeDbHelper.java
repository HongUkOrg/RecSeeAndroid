package com.example.user.database.DB_UTIL;


import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import com.example.user.database.Adapter.Call_Model;
import com.example.user.database.Adapter.Contacts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeDbHelper extends SQLiteOpenHelper
{
    private static TimeDbHelper kInstance;


    private static final int DB_VERSION = 12;
    public static final String DB_NAME = "memo.db";

    public static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s TEXT" +




                            ")",

                    "updatetime",
                    Call_Model.Callentry._ID,
                    "time"


            );





    public static TimeDbHelper getInstance(Context context) //factory method 외부에서 인스턴스를 받기
    {
        if(kInstance==null)
        {
            kInstance = new TimeDbHelper(context);
        }
        return kInstance;

    }

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+ "updatetime";
    public TimeDbHelper(Context context)
    {

        super(context,DB_NAME,null,DB_VERSION);


    }


    public void save_Contact(Contacts contact)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String start = dateFormat.format(contact.getStart());
        String end = dateFormat.format(contact.getEnd());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Call_Model.Callentry.COLUMN_NAME_CONTENTS, contact.getNumber());
        values.put(Call_Model.Callentry.COLUMN_NAME_TITLE,contact.getName());



        db.insert(Call_Model.Callentry.TABLE_NAME, null, values);
        db.close(); // Closing database connection




    }




    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try{
            db.execSQL(SQL_CREATE_ENTRIES);

        }catch(Exception e)
        {
            e.printStackTrace();
        }



    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion)
    {
        try {
            db.execSQL(SQL_DELETE_ENTRIES);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        onCreate(db);

    }

}
