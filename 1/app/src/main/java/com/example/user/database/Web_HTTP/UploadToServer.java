package com.example.user.database.Web_HTTP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.database.Adapter.Call_Model;
import com.example.user.database.Adapter.SettingRecyclerAdapter;
import com.example.user.database.DB_UTIL.MemoDbHelper;
import com.example.user.database.IconTabsActivity;
import com.example.user.database.MainActivity;
import com.example.user.database.R;
import com.example.user.database.SettingActivity;
import com.example.user.database.base.AdapterDelegate;
import com.example.user.database.model.SettingItem;
import com.example.user.database.tab_main;
import com.skyfishjy.library.RippleBackground;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class UploadToServer extends Activity {

    public static final String TAG = "uploadToServer TAG";

    TextView messageText;
    Button uploadButton;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    private  HttpURLConnection http;

    HttpClient client;

    String upLoadServerUri = null;
    String all_upload = null;
    long id=-1;

    /**********  File Path *************/
    final String uploadFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()
            +"/MyRecSee/";
    String uploadFileName = "hongcha.mp3";
    private boolean all_complete;
    private boolean wifi_only_upload = false;

    private TextView result_post;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);






        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        String start_time=null;

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();

        result_post = (TextView)findViewById(R.id.requestTextView);


        Intent intent = getIntent();
        all_complete=false;

        uploadButton = (Button)findViewById(R.id.uploadButton);


        if (intent != null)
        {
            id = intent.getLongExtra("id",-1);
            all_upload = intent.getStringExtra("all");


            start_time = intent.getStringExtra("StartTime");

            uploadFileName="All files are not uploaded";




        }

            if(id != -1 && start_time != null  )
            {

                Log.d("uploadToServer", "onCreate: passed");

                uploadButton.setText("개별 전송하기");


                String child = "record_";
                String extension = ".mp3";

                child += start_time;
                child += extension;

                uploadFileName=child;






        }








        messageText  = (TextView)findViewById(R.id.messageText);

        messageText.setText("업로드 예정 파일 \n==> "+"'"+uploadFileName+"'");

        /************* Php script path ****************/
        upLoadServerUri = "http://phwysl.dothome.co.kr/pass.php";
        uploadButton.setOnClickListener(new OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {

                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected()) {

                }
                else
                {
                    if(SettingActivity.wifi_upload_checked==true)
                    {
                        Toast.makeText(UploadToServer.this,"WIFI_ONLY 모드입니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }



                if(all_upload!=null)
                {
                    if (all_upload.equals("OK"))
                    {
                        HttpPostData(-1);

                    }
                }
                else
                {

                    HttpPostData(id);
                }





                dialog = ProgressDialog.show(UploadToServer.this, "", "Uploading file...", true);

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                messageText.setText("uploading started.....");
                            }
                        });
                        if(all_upload!=null)
                        {
                            if (all_upload.equals("OK"))
                            {
                                upload_all_file();

                            }
                        }
                        else
                            {

                            uploadFile(uploadFilePath + "" + uploadFileName);
                        }

                    }
                }).start();


            }
        });


        Button post_button = (Button)findViewById(R.id.postButton);
        post_button.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view)
            {

                HttpPostData(-1);

            }
        });

    }

    private void upload_all_file()
    {


        Cursor cursor = get_not_uploaded_file();

        int i=0;
        while (cursor.moveToNext()) {

            Log.d(TAG, "get_not_uploaded_file: "+i+"th call : "+cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry._ID)));
            i++;



            uploadFile(uploadFilePath+"record_"+cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_START_TIME))+".mp3");

            update_file_upload_ok(cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry._ID)));

        }


            all_upload=null;


            //startActivity(new Intent(UploadToServer.this, tab_main.class));




    }

    public Cursor get_not_uploaded_file()
    {
        String result;
        MemoDbHelper dbHelper = MemoDbHelper.getInstance(this);
        Cursor cursor;

        return dbHelper.getReadableDatabase()
                .query(Call_Model.Callentry.TABLE_NAME, null,"upload='NO' AND file_exist='OK'",null,null,null,null);





    }

    public int uploadFile(final String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    +uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"
                            +uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                + fileName + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                Log.d(TAG, "Upload Code: "+conn+conn.getResponseCode());
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            if(id!=-1)
                            {
                                update_file_upload_ok(Long.toString(id));
                            }


                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/"
                                    +sourceFileUri;

                            messageText.setText(msg);
                            Toast.makeText(UploadToServer.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();


                                if (all_upload!=null&&all_upload.equals("OK"))
                                {

                                }
                                else {
                                    //startActivity(new Intent(UploadToServer.this, tab_main.class));
                                }

                        }
                    });
                }

                //close the streams //
                fileInputStream.close();

                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UploadToServer.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UploadToServer.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file", "Exception : "
                        + e.getMessage(), e);
            }
            conn.disconnect();
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }


    void update_file_upload_ok(String id)
    {

        MemoDbHelper memo = new MemoDbHelper(this);
        SQLiteDatabase db =  memo.getWritableDatabase();


        String strFilter = "_id=" + id;
        ContentValues args = new ContentValues();
        args.put(Call_Model.Callentry.COLUMN_NAME_UPLOAD,"OK");
        long result = db.update("contact", args, strFilter, null);
        Log.d("DB UPDATE RESULT", "update_file_upload_ok: " + result);



    }
    void update_json_post_ok(String id)
    {

        MemoDbHelper memo = new MemoDbHelper(this);
        SQLiteDatabase db =  memo.getWritableDatabase();


        String strFilter = "_id=" + id;
        ContentValues args = new ContentValues();
        args.put(Call_Model.Callentry.COLUMN_NAME_INFO_POST,"OK");
        long result = db.update("contact", args, strFilter, null);
        Log.d("DB UPDATE RESULT", "update_file_upload_ok: " + result);



    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    public void HttpPostData(long id) {
        try {

            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://phwysl.dothome.co.kr/logout.php");
            // URL 설정
            http = (HttpURLConnection) url.openConnection();   // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //--------------------------
            //   서버로 값 전송
            //--------------------------


            ContentValues contentValues = new ContentValues();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");



            String current = dateFormat.format(new Date());
            contentValues.put("time", current);




            JSONObject db_update_json= new JSONObject();
            String myJson=null;


            if(id == -1)
            {

                db_update_json = get_db_json(get_cursor());


                myJson = db_update_json.toString();

            }
            else
            {

                db_update_json = get_db_json(get_cursor_one_object(id));


                myJson = db_update_json.toString();

            }


            Log.d(TAG, "HttpPostData: data is"+myJson);





// file upload test를 위해 잠시 주석 처리

            StringBuffer buffer = new StringBuffer();
            buffer.append("json=");
            buffer.append(myJson);
//
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();



            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;



            if(http.getResponseCode()==200)
            {
                for(int i=0;i<db_update_json.length();i++)
                {
                    JSONObject obj = db_update_json.getJSONObject(Integer.toString(i+1));

                    update_json_post_ok(obj.getString("id"));
                    //startActivity(new Intent(UploadToServer.this,tab_main.class));
                }

            }



            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            String myResult = builder.toString();
            // 전송결과를 전역 변수에 저장
            result_post.setText(myResult);
//            ((TextView)(findViewById(R.id.requestTextView))).setText(myResult);


//            Toast.makeText(UploadToServer.this, "전송 후 결과 받음", 0).show();
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
        catch (JSONException e) {
            e.printStackTrace();
        }

        http.disconnect();

    } // HttpPostData




    public Cursor get_cursor()
    {
        MemoDbHelper dbHelper = MemoDbHelper.getInstance(this);
        return dbHelper.getReadableDatabase()
                .query(Call_Model.Callentry.TABLE_NAME,
                        null,null,null,null,null,null);



    }

    public JSONObject get_db_json(Cursor cursor) throws JSONException
    {


        Cursor c = cursor;
        JSONObject result_json = new JSONObject();


        while (c.moveToNext()) {

            JSONObject myobj = new JSONObject();


            String id = c.getString(c.getColumnIndexOrThrow(Call_Model.Callentry._ID));
            String phone = c.getString(c.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_CONTENTS));
            String state = c.getString(c.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_IN_OR_OUT));
            String name = c.getString(c.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_TITLE));
            String start = c.getString(c.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_START_TIME));
            String end = c.getString(c.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_END_TIME));



            try
            {
                myobj.put("id",id);
                myobj.put("phone",phone);
                myobj.put("name",name);
                myobj.put("state",state);
                myobj.put("start_time",start);
                myobj.put("end_time",end);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            result_json.put(id,myobj);

        }




        return result_json;



    }


    public Cursor get_cursor_one_object(long id)
    {

        MemoDbHelper dbHelper = MemoDbHelper.getInstance(this);
        return dbHelper.getReadableDatabase()
                .query(Call_Model.Callentry.TABLE_NAME,
                        null, Call_Model.Callentry._ID+"='"+Long.toString(id)+"'",null,null,null,null);


    }
}