package com.example.user.database.DB_UTIL;



import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import com.example.user.database.Adapter.Call_Model;
import com.example.user.database.Adapter.Contacts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MemoDbHelper extends SQLiteOpenHelper
{
    private static MemoDbHelper sInstance; // memo db 헬퍼는 하나의 인스턴스만 가져도되서 스태틱으로 선언.


    private static final int DB_VERSION = 23;
    public static final String DB_NAME = "memo.db";

    public static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s TEXT," +
                            "%s TEXT," +
                            "%s TEXT," +
                            "%s TEXT," +
                            "%s TEXT," +
                            "%s TEXT," +
                            "%s TEXT," +
                            "%s TEXT," +
                            "%s TEXT"  +

            ")",

                    Call_Model.Callentry.TABLE_NAME,
                    Call_Model.Callentry._ID,
                    Call_Model.Callentry.COLUMN_NAME_CONTENTS,
                    Call_Model.Callentry.COLUMN_NAME_TITLE,
                    Call_Model.Callentry.COLUMN_NAME_START_TIME,
                    Call_Model.Callentry.COLUMN_NAME_END_TIME,
                    Call_Model.Callentry.COLUMN_NAME_IN_OR_OUT,
                    Call_Model.Callentry.COLUMN_NAME_MISS_REASON,
                    Call_Model.Callentry.COLUMN_NAME_FILE_EXIST,
                    Call_Model.Callentry.COLUMN_NAME_UPLOAD,
                    Call_Model.Callentry.COLUMN_NAME_INFO_POST



            );
    public static final String SQL_CREATE_ENTRIES2 =
            String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s TEXT" +




                            ")",

                    "updatetime",
                    Call_Model.Callentry._ID,
                    "time"


            );







    public static MemoDbHelper getInstance(Context context) //factory method 외부에서 인스턴스를 받기
    {
        if(sInstance==null)
        {
            sInstance = new MemoDbHelper(context);
        }
        return sInstance;

    }

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+
            Call_Model.Callentry.TABLE_NAME;
    public static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS "+
            "updatetime";
    public MemoDbHelper(Context context)
    {

        super(context,DB_NAME,null,DB_VERSION);


    }


    public void save_Contact(Contacts contact)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh:mm:ss");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String start = dateFormat.format(contact.getStart());
        String end = dateFormat.format(contact.getEnd());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Call_Model.Callentry.COLUMN_NAME_CONTENTS, contact.getNumber());
        values.put(Call_Model.Callentry.COLUMN_NAME_TITLE,contact.getName());
        values.put(Call_Model.Callentry.COLUMN_NAME_START_TIME,start);
        values.put(Call_Model.Callentry.COLUMN_NAME_END_TIME,end);
        values.put(Call_Model.Callentry.COLUMN_NAME_IN_OR_OUT,contact.getIn_or_out());
        values.put(Call_Model.Callentry.COLUMN_NAME_MISS_REASON,contact.get_miss_reason());
        values.put(Call_Model.Callentry.COLUMN_NAME_FILE_EXIST,contact.getVoiceRecord());
        values.put(Call_Model.Callentry.COLUMN_NAME_UPLOAD,contact.getIs_uploaded());
        values.put(Call_Model.Callentry.COLUMN_NAME_INFO_POST,contact.get_info_posted());



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
            db.execSQL(SQL_DELETE_ENTRIES2);

        }catch(Exception e)
        {
            e.printStackTrace();
        }

        onCreate(db);

    }

}
