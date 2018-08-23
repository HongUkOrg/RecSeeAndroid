package com.example.user.database.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.database.R;
import com.example.user.database.base.AdapterDelegate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.user.database.R.drawable.ic_star_border_blue_24dp;

public class CallAdapter extends CursorAdapter
{


    private static boolean select=false;






    public CallAdapter(Context context, Cursor c)
    {


        super(context, c, false);
    }


    public static void set_select_true()
    {

        select = true;

    }
    public static void set_select_false()
    {

        select = false;

    }


    public Object getItem(int position) {
        return super.getItem(position);
    }




    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context)
                .inflate(R.layout.contact_list,parent,false);
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {


        Log.d("바인드뷰", "bindView() called!");

        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBox1);



        if(select)
        {
            checkBox.setVisibility(View.VISIBLE);

        }
        else
        {
            checkBox.setVisibility(View.INVISIBLE);
        }




        ImageView voice_img = (ImageView) view.findViewById(R.id.imageView5);
        ImageView upload_img = (ImageView) view.findViewById(R.id.imageView);
        ImageView info_upload_imag = (ImageView)view.findViewById(R.id.imageView2) ;
//        ImageView profile_image = (ImageView)view.findViewById(R.id.profile_image);
//
//        int profile_ID = 0;
//        profile_ID = (int)cursor.getLong(cursor.getColumnIndexOrThrow(Call_Model.Callentry._ID));
//
//        profile_ID = profile_ID % 8 ;
//
//        switch(profile_ID)
//        {
//            case 1:
//                profile_image.setImageResource(R.drawable.icon_hong_1);
//                break;
//             case 2:
//                profile_image.setImageResource(R.drawable.icon_hong_2);
//                break;
//             case 3:
//                profile_image.setImageResource(R.drawable.icon_hong_3);
//                break;
//             case 4:
//                profile_image.setImageResource(R.drawable.icon_hong_4);
//                break;
//             case 5:
//                profile_image.setImageResource(R.drawable.icon_hong_5);
//                break;
//             case 6:
//                profile_image.setImageResource(R.drawable.icon_hong_6);
//                break;
//             case 7:
//                profile_image.setImageResource(R.drawable.icon_hong_7);
//                break;
//             case 0:
//                profile_image.setImageResource(R.drawable.icon_hong_0);
//                break;
//
//            default:
//                break;
//
//
//
//        }





        String record = "";
        String upload = "";
        String in_out="";
        String post_ok="";

        long min =0;
        long sec =0;

        long call_length;
        String input_length="";

        String start = cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_START_TIME));
        String end = cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_END_TIME));

        Date date=new Date();
        Date date2=new Date();
        try {
            date = new SimpleDateFormat("yyyy_MM_dd_hh:mm:ss").parse(start);
             date2 = new SimpleDateFormat("yyyy_MM_dd_hh:mm:ss").parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        call_length = date2.getTime()-date.getTime();

        call_length /=1000;

        if(call_length>=60)
        {

            min=call_length/60;
            sec=call_length%60;



            input_length += Long.toString(min);
            input_length += "m ";

            input_length += Long.toString(sec);
            input_length += "s";


        }
        else {

            if(call_length==0)
            {
                input_length = "Call Missed";
            }

            else {
                input_length += Long.toString(call_length);
                input_length += "s";
            }

        }

        String first_text="";


        TextView startTime_textview = (TextView)view.findViewById(R.id.profile_image);
//        first_text=cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry._ID));
//        first_text+="\n";

        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd\nhh:mm:ss");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String start_time_parsed = dateFormat.format(date);
        first_text+=start_time_parsed;

        startTime_textview.setText(first_text);


        record = cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_FILE_EXIST));
        upload = cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_UPLOAD));
        post_ok = cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_INFO_POST));


//
//        Log.d("check adapter", "bindView: "+upload +" id is " +
//                cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry._ID))+ " record is "+
//        record+
//                "    "+cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_CONTENTS)));


        in_out=cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_IN_OR_OUT));

        TextView titleText = (TextView)view.findViewById(R.id.textView3);
        titleText.setText(input_length);

        TextView titleText2 = (TextView)view.findViewById(R.id.textView2);
        TextView titleText3 = (TextView)view.findViewById(R.id.textView4);
        titleText2.setText(cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_CONTENTS)));
        titleText3.setText(in_out);


        if(record.equals("OK")) {

            voice_img.setImageResource(R.drawable.ic_microphone_blue);

        }
        else
        {
            voice_img.setImageResource(R.drawable.ic_microphone);
        }

        if(upload.equals("OK"))
        {

            upload_img.setImageResource(R.drawable.icon_menu_file_over);

        }
        else
        {
            upload_img.setImageResource(R.drawable.icon_menu_file);
        }

        if(post_ok.equals("OK"))
        {

            info_upload_imag.setImageResource(R.drawable.post_icon_ok);

        }
        else
        {
            info_upload_imag.setImageResource(R.drawable.post_icon);
        }


    }



}