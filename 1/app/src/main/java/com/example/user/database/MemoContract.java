package com.example.user.database;



import android.provider.BaseColumns;

public class MemoContract
{


    private MemoContract()
    {


    }

    public static class memoentry implements BaseColumns

    {

        public static final String USER_ID = "user_id";
        public static final String USER_PW = "user_PW";

        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_TITLE="user_name";
        public static final String COLUMN_NAME_CONTENTS="phone_number";
        public boolean IN_OR_OUT = false;
    }
}

