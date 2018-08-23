package com.example.user.database;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.database.Adapter.CallAdapter;
import com.example.user.database.Adapter.Call_Model;
import com.example.user.database.Adapter.Contacts;
import com.example.user.database.DB_UTIL.MemoDbHelper;
import com.example.user.database.Fragment.TwoFragment;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_INSERT = 1000;


    private ArrayAdapter<String> db_adapter;
    private List<String> db_update_data;

    private boolean select = false;
    private boolean move_page = false;
    private boolean click_list[];

    private static final String TAG = "Sample";

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";

    private com.example.user.database.DatePicker dateTimeFragment;
    private com.example.user.database.DatePicker dateTimeFragment2;




    FloatingActionButton fb;
    ListView db_list;



    int button_toggle = 1; // index for btn_toggle
    public static CallAdapter callAdapter;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        input_test_thausand();

        changeView(0);



        final Button date_btn = (Button)findViewById(R.id.date_btn);

        dateTimeFragment = (com.example.user.database.DatePicker) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment == null) {
            dateTimeFragment = com.example.user.database.DatePicker.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel),
                    getString(R.string.clean) // Optional
            );
        }

        // Optionally define a timezone
        dateTimeFragment.setTimeZone(TimeZone.getDefault());

        // Init format
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd MM YYYY", java.util.Locale.getDefault());
        // Assign unmodifiable values
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setHighlightAMPMSelection(false);

        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());

        // Define new day and month format
        try
        {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (DatePicker.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }
        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragment.setOnButtonClickListener(new DatePicker.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date)
            {
                TextView start_time = (TextView)findViewById(R.id.start_time);

                start_time.setText(myDateFormat.format(date));



                        dateTimeFragment2.startAtCalendarView();
                        dateTimeFragment2.setDefaultDateTime(new GregorianCalendar(2018, Calendar.MARCH, 4, 15, 20).getTime());
                        dateTimeFragment2.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);




            }

            @Override
            public void onNegativeButtonClick(Date date)
            {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists
//                textView.setText("");
            }
        });


        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Re-init each time
                dateTimeFragment.startAtCalendarView();
                dateTimeFragment.setDefaultDateTime(new GregorianCalendar(2018, Calendar.MARCH, 4, 15, 20).getTime());
                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
        });









        dateTimeFragment2 = (com.example.user.database.DatePicker) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment2 == null) {
            dateTimeFragment2 = com.example.user.database.DatePicker.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel),
                    getString(R.string.clean) // Optional
            );
        }

        // Optionally define a timezone
        dateTimeFragment2.setTimeZone(TimeZone.getDefault());

        // Init format
        final SimpleDateFormat myDateFormat2 = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());
        // Assign unmodifiable values
        dateTimeFragment2.set24HoursMode(false);
        dateTimeFragment2.setHighlightAMPMSelection(false);

        dateTimeFragment2.setMinimumDateTime(new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime());
        dateTimeFragment2.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());

        // Define new day and month format
        try
        {
            dateTimeFragment2.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (DatePicker.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }
        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragment2.setOnButtonClickListener(new DatePicker.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date)
            {
                TextView end_time = (TextView)findViewById(R.id.end_time);

                end_time.setText(myDateFormat.format(date));




            }

            @Override
            public void onNegativeButtonClick(Date date)
            {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists
//                textView.setText("");
            }
        });



    }







    private void changeView(int index)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout contents = (RelativeLayout) findViewById(R.id.contents);


        if (contents.getChildCount() > 0)
        {
            contents.removeViewAt(0);
        }

        switch (index) {
            case 0:
                LinearLayout call_list = (LinearLayout) inflater.inflate(R.layout.call_list, null);
                contents.addView(call_list);



                final ListView hong = (ListView) findViewById(R.id.hong);

                final Cursor cursor = getCallCursor();



                callAdapter = new CallAdapter(MainActivity.this, cursor);

                if(select)
                {
                    callAdapter.set_select_true();
                }
                else
                {
                    callAdapter.set_select_false();
                }

                hong.setAdapter(callAdapter);





                if(select ==false)
                {


                    hong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(MainActivity.this, MemoActivity.class);

                            Cursor cursor = (Cursor) callAdapter.getItem(position);

                            String title = cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_TITLE));
                            String contents = cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_CONTENTS));
                            String miss_reason = cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_MISS_REASON));

                            String start_time = cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_START_TIME));

                            intent.putExtra("id", id);
                            intent.putExtra("title", title);
                            intent.putExtra("contents", contents);
                            intent.putExtra("miss_reason", miss_reason);
                            intent.putExtra("start_time", start_time);


                            startActivityForResult(intent, REQUEST_CODE_INSERT);

                        }
                    });


                    final ImageView search_btn = (ImageView) findViewById(R.id.search_btn);


                    search_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {





                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Please Enter Phone number");
                            alertDialog.setMessage("what you want to search");
                            final EditText et = new EditText(MainActivity.this);
                            alertDialog.setView(et);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which)
                                        {

                                            String value = et.getText().toString();

                                            callAdapter.swapCursor(search("",value));

                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();

                        }
                    });





                }
                else
                {






                }

                final Button select_btn = (Button)findViewById(R.id.select_btn);

                select_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {


                        if(select == true)
                        {
                            SparseBooleanArray checkedItems = hong.getCheckedItemPositions();
                            int count = callAdapter.getCount();

                            ArrayList<Integer> arr = new ArrayList<Integer>();

                            for (int i = count - 1; i >= 0; i--)
                            {
                                if (checkedItems.get(i))
                                {


                                    arr.add(count-(i));
//                                    Log.d("선택된 아이템은", "onClick: " + (count-(i+1)));
                                }
                            }


                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            Fragment fragment = new TwoFragment();
                            Bundle bundle = new Bundle(1);
                            bundle.putIntegerArrayList("post", arr);
                            fragment.setArguments(bundle);

                            transaction.replace(R.id.contents, fragment);

                            transaction.commit();
                            contents.removeViewAt(0);

                            Button button = (Button)findViewById(R.id.select_btn);
                            select_btn.setBackgroundColor(Color.rgb(100,100,0));
                            select_btn.setText("BACK");
                            move_page = true;
                            select = false;



                        }
                        else if(select==false&&move_page==false)
                        {

                            select_btn.setBackgroundColor(Color.rgb(200,0,0));
                            select_btn.setText("COMFIRM");
                            select = true;
                            Toast.makeText(MainActivity.this,"Select Mode Active!",Toast.LENGTH_SHORT).show();
                            changeView(0);


                        }
                        else
                        {
                            select = false;
                            move_page = false;
                            select_btn.setText("SELECT");
                            select_btn.setBackgroundColor(Color.rgb(160,160,160));
                            changeView(0);


                        }







                    }
                });


//                hong.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                        final long deleteId = id;
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setTitle("memo del");
//                        builder.setMessage("memo delete?");
//                        builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        SQLiteDatabase db = MemoDbHelper.getInstance(MainActivity.this).getWritableDatabase();
//                                        int deletedCount = db.delete(Call_Model.Callentry.TABLE_NAME,
//                                                Call_Model.Callentry._ID + "=" + deleteId, null);
//                                        if (deletedCount == 0) {
//                                            Toast.makeText(MainActivity.this, "del error", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            callAdapter.swapCursor(getCallCursor());
//                                            Toast.makeText(MainActivity.this, "delete success", Toast.LENGTH_SHORT).show();
//                                        }
//
//
//                                    }
//                                }
//                        );
//                        builder.setNegativeButton("취소", null);
//                        builder.show();
//                        return true;
//                    }
//                });




                break;

                default:
                    break;


        }


    }




    private void initDate()
    {
        db_update_data = new ArrayList<>();

        Cursor cursor = getTimeCursor();


        String result;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력

        while (cursor.moveToNext()) {
            result ="";
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " updated "
                   ;

            db_update_data.add(result);

        }






    }


    private void initAdapter()
    {
        db_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,db_update_data);
    }
    private void initListView()
    {
        ListView db_list_view = (ListView)findViewById(R.id.db_update_list);
        db_list_view.setAdapter(db_adapter);

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_INSERT && resultCode == RESULT_OK)
        {
            callAdapter.swapCursor(getCallCursor());

        }
    }


    public Cursor getCallCursor()
    {
        MemoDbHelper dbHelper = MemoDbHelper.getInstance(this);
        return dbHelper.getReadableDatabase()
                .query(Call_Model.Callentry.TABLE_NAME,
                        null,null,null,null,null,"_id DESC");
    }

    public final static void updateCursor(Context context, Cursor cursor)
    {
        //CallAdapter call =



        //MainActivity.this= (MainActivity) context;
        //callAdapter.notifyDataSetChanged();
        callAdapter.swapCursor(cursor);
        //ListView hong = (ListView)findViewById(R.id.hong);
        //hong.invalidateViews();

    }






    public Cursor getTimeCursor()
    {


        MemoDbHelper dbHelper = MemoDbHelper.getInstance(this);
        return dbHelper.getReadableDatabase()
                .query("updatetime",
                        null,null,null,null,null,null);

    }


    public Cursor search(String date,String phone_number)
    {


        MemoDbHelper dbHelper = MemoDbHelper.getInstance(this);
        Cursor cursor = dbHelper.getReadableDatabase()
                .query(Call_Model.Callentry.TABLE_NAME,
                        null,Call_Model.Callentry.COLUMN_NAME_CONTENTS+"='"+phone_number+"'", null,null,null,null);










        return cursor;
    }
    public void input_test_thausand()
    {


        MemoDbHelper dbHelper = MemoDbHelper.getInstance(this);
        for(int i=1; i<1000;i++)
        {



            dbHelper.save_Contact(new Contacts("hongcha",Integer.toString(i),new Date(),new Date(),"IN","","OK","NO","NO"));





        }




    }









}
