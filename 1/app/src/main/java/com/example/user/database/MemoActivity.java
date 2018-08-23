package com.example.user.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.database.Adapter.Call_Model;
import com.example.user.database.DB_UTIL.MemoDbHelper;
import com.example.user.database.Web_HTTP.UploadToServer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MemoActivity extends AppCompatActivity {

    private static final String TAG = "sdaf";
    private TextView mTitleEditText;
    private TextView mContentsEditText;
    private TextView miss_reasonTExt;
    private long mMemoId= -1;
    private String saved_num;
    private Boolean isPlaying = false;
    private Boolean pause = false;
    String start_time="";
    MemoDbHelper memoDbHelper;
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_of_call);

        File storeDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyRecSee");

//        myMediaPlayer = MediaPlayer.create(this,storeDir)''




        mTitleEditText = (TextView)findViewById(R.id.title_edit);
        mContentsEditText=(TextView)findViewById(R.id.contents_edit);
        miss_reasonTExt = (TextView)findViewById(R.id.miss_call_reason);



        Intent intent = getIntent();
        if (intent != null)
        {
            mMemoId = intent.getLongExtra("id",-1);
            String title = intent.getStringExtra("title");
            String contents = intent.getStringExtra("contents");
            String miss_reason = intent.getStringExtra("miss_reason");
            start_time = intent.getStringExtra("start_time");

            saved_num=contents;

            mTitleEditText.setText(title);
            mContentsEditText.setText(contents);
            miss_reasonTExt.setText(miss_reason);

        }

        Button call_to_customer = (Button) findViewById(R.id.call_to_customer);

        call_to_customer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

//                SQLiteDatabase db = MemoDbHelper.getInstance(LoginActivity.this).getReadableDatabase();




                String tel = "tel:"+saved_num;
                startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));


            }

        });

        final Button play_button = (Button) findViewById(R.id.play_recorded_button);
        Button uplaod_button = (Button) findViewById(R.id.one_upload_button);


        uplaod_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(MemoActivity.this, UploadToServer.class);
                intent.putExtra("StartTime", start_time);
                intent.putExtra("id",mMemoId);
//
                startActivity(intent);

            }
        });


        play_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {



                String child = "record_";

                String extension = ".mp3";

                child += start_time;
                child += extension;


                File storeDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyRecSee/"+child);


                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
                filePath += "/MyRecSee/"+child;

                Log.d(TAG, "file_play: "+filePath);
                // initialize Uri here

//                Intent i = new Intent(MemoActivity.this,PlayerProgress.class);
//                i.putExtra("path",filePath);
//                startActivity(i);

                playmusic(view.getContext(),filePath,null);



                //private play 주석 처리 ! << playerProgress activity 로 대체
//                dialog.dismiss();


//
//                if ( player == null )
//                {
//                    player = new MediaPlayer();
//                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        public void onCompletion(MediaPlayer mp) {
//                            mp.stop();
//                            mp.release();
//                            play_button.setText("play");
//                            mp=null;
//                        }
//                    });
//                }
//
//                if(player.isPlaying())
//                {
//                    player.pause();
//                    pause=true;
//                    play_button.setText("Paused");
//
//
//
//
//                    return;
//                }
//                if(pause)
//                {
//                    player.start();
//                    pause=false;
//                    play_button.setText("playing again");
//                    return;
//                }
//
//                File sourceFile = new File(filePath);
//                if ( sourceFile.exists() )
//                {
//                    FileInputStream fs = null;
//                    try {
//                        fs = new FileInputStream(filePath);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    FileDescriptor fd = null;
//                    try {
//                        fd = fs.getFD();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        player.setDataSource(fd);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        fs.close(); // 데이터 소스를 설정한 후 스트림을 닫았다.
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        player.prepare();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//
//                        player.start();
//
//                    play_button.setText("playing");
//
//                }
//                else
//                {
//                    Toast.makeText(MemoActivity.this,"File does not eixst",Toast.LENGTH_SHORT).show();
//                }
//
//







//                MediaPlayer mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                try {
//                    mediaPlayer.setDataSource(filePath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    mediaPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if(!isPlaying)
//                {
//                    mediaPlayer.start();
//
//                    Toast.makeText(MemoActivity.this,child+" is played well!",Toast.LENGTH_LONG).show();
//                    isPlaying=true;
//                }
//                else
//                {
//                    mediaPlayer.stop();
//                    Toast.makeText(MemoActivity.this,child+" shut down!",Toast.LENGTH_LONG).show();
//                    isPlaying=false;
//                }


            }

        });



    }

    public static void playmusic(Context ctx, String path, String str){
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(path);
        Uri fileuri = FileProvider.getUriForFile(ctx,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        intent.setDataAndType(fileuri, "audio/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try{
            ctx.startActivity(intent);
        }catch (Exception e){
//            Toast.makeText(ctx, "Media player not found.", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(ctx,PlayerProgress.class);
            i.putExtra("PATH",path);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            ctx.startActivity(i);
        }
    }

    @Override public void onBackPressed()
    {
        String title = mTitleEditText.getText().toString();
        String contents = mContentsEditText.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Call_Model.Callentry.COLUMN_NAME_CONTENTS,contents);
        contentValues.put(Call_Model.Callentry.COLUMN_NAME_TITLE,title);

        //key value 저장하는 함수

        SQLiteDatabase db = MemoDbHelper.getInstance(this).getWritableDatabase();

        if(mMemoId == -1)
        {
            long newRowId = db.insert(Call_Model.Callentry.TABLE_NAME,
                    null,
                    contentValues);
            if(newRowId==-1)
            {
                Toast.makeText(this,"error occured",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"well",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }

        }
        else
        {
            int count = db.update(Call_Model.Callentry.TABLE_NAME,contentValues,
                    Call_Model.Callentry._ID+"="+mMemoId,null);
            if(count==0)
            {
                Toast.makeText(this,"수정 error",Toast.LENGTH_SHORT).show();

            }
            else
            {

                Toast.makeText(this,"Call Updated",Toast.LENGTH_SHORT).show();
            }


        }





        super.onBackPressed();



    }
}
