package com.example.user.database.Receiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.user.database.Adapter.Call_Model;
import com.example.user.database.Adapter.Contacts;
import com.example.user.database.MainActivity;
import com.example.user.database.DB_UTIL.MemoDbHelper;
import com.example.user.database.tab_main;

import java.util.Date;


// ALERT DIALOG
// Sources : http://techblogon.com/alert-dialog-with-edittext-in-android-example-with-source-code/





public class AlertDialogActivity extends Activity
{

    MainActivity main;

    private static final String TAG = "alert";
    String name,phone,start,state;

    private AlertDialog myDialog;

    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null)
        {
            name = intent.getStringExtra("name");
            phone = intent.getStringExtra("phone");
            state = intent.getStringExtra("state");



        }



        if ((this != null) ) {
            AlertDialog miss_dialog;

            AlertDialog.Builder ad = new AlertDialog.Builder(this);


            ad.setTitle("전화 실패 사유를 기입해주세요");       // 제목 설정
            ad.setMessage("이유");   // 내용 설정

            // EditText 삽입하기
            final EditText et = new EditText(this);
            ad.setView(et);


// 확인 버튼 설정
            AlertDialog.Builder builder = ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which){
                    Log.v(TAG, "Yes Btn Click");

                    // Text 값 받아서 로그 남기기
                    String value = et.getText().toString();
                    Log.v(TAG, value);



                    MemoDbHelper dbHelper = MemoDbHelper.getInstance(AlertDialogActivity.this);
                    dbHelper.save_Contact(new Contacts(name, phone, new Date(), new Date(), state, value,"NO","NO","NO"));
                    //main=main.set_context(ctx);
                    main.updateCursor(AlertDialogActivity.this, getChangedCursor(AlertDialogActivity.this));


                    //this_dialog.dismiss();
                    dialog.dismiss();
                    //myDialog.dismiss();     //닫기
                    // Event
                    Intent i = new Intent(AlertDialogActivity.this, tab_main.class);
                    startActivity(i);
                }
            });

// 중립 버튼 설정
            ad.setNeutralButton("What?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.v(TAG, "Neutral Btn Click");
                    dialog.dismiss();     //닫기
                    AlertDialogActivity.this.finish();
                    // Event
                }
            });

// 취소 버튼 설정
            ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {




                    MemoDbHelper dbHelper = MemoDbHelper.getInstance(AlertDialogActivity.this);
                    dbHelper.save_Contact(new Contacts(name, phone, new Date(), new Date(), state, "nothing","NO","NO","NO"));
                    //main=main.set_context(ctx);
                    main.updateCursor(AlertDialogActivity.this, getChangedCursor(AlertDialogActivity.this));


                    //this_dialog.dismiss();
                    dialog.dismiss();
                    //myDialog.dismiss();     //닫기
                    Intent i = new Intent(AlertDialogActivity.this, tab_main.class);
                    startActivity(i);
                }
            });



            ad.create().show();


        }
    }

    public Cursor getChangedCursor(Context ctx)
    {

        MemoDbHelper dbHelper = MemoDbHelper.getInstance(ctx);
        return dbHelper.getReadableDatabase()
                .query(Call_Model.Callentry.TABLE_NAME,
                        null,null,null,null,null,"_id DESC");

    }
}


