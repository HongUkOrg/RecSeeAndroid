package com.example.user.database.Adapter;



import android.provider.BaseColumns;

public class Call_Model
{


    private Call_Model()
    {


    }

    public static class Callentry implements BaseColumns

    {

        public static final String USER_ID = "user_id";
        public static final String USER_PW = "user_pw";

        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_TITLE="user_name";
        public static final String COLUMN_NAME_CONTENTS="phone_number";
        public static final String COLUMN_NAME_START_TIME="call_start_time";
        public static final String COLUMN_NAME_END_TIME="call_end_time";
        public static final String COLUMN_NAME_IN_OR_OUT="in_or_out";
        public static final String COLUMN_NAME_MISS_REASON="miss_reason";
        public static final String COLUMN_NAME_UPLOAD="upload";
        public static final String COLUMN_NAME_FILE_EXIST="file_exist";
        public static final String COLUMN_NAME_INFO_POST="post_info";


    }
}

