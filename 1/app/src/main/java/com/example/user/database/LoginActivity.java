package com.example.user.database;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.database.DB_UTIL.MemoDbHelper;
import com.example.user.database.Web_Socket.Socket_IO;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {


    public static final String CMD = "cmd";
    public static final String DIAL_REQ = "dial_mobile_req";
    public static final String DIAL_ACK = "dial_mobile_ack";
    public static final String DROP_REQ = "drop_mobile_req";
    public static final String DROP_ACK = "drop_mobile_ack";
    public static final String LOGIN_REQ = "login_mobile_req";
    public static final String LOGIN_ACK = "login_mobile_ack";
    public static final String LOGOUT_REQ = "logout_mobile_req";
    public static final String LOGOUT_ACK = "logout_mobile_ack";

    static final int login_id = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2001;

    public static final String NOTIFICATION_CHANNEL_ID_SERVICE = "com.mypackage.service";
    public static final String NOTIFICATION_CHANNEL_ID_TASK = "com.mypackage.download_info";
    private static String id_content;
    private static boolean is_login = false;


    private String pw_content="";
    private boolean ean;

    public void initChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, "App Service", NotificationManager.IMPORTANCE_DEFAULT));
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_TASK, "Download Info", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }


    public static Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://whispering-forest-76925.herokuapp.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initChannel();

        Socket_IO socket_io = new Socket_IO();


        mSocket.on("call to customer", onNewMessage);
        mSocket.connect();





//
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.android.server.telecom","com.android.server.telecom.settings.EnableAccountPreferenceActivity"));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        this.startActivity(intent);

        ean = false;

        ean = NotificationManagerCompat.from(this).areNotificationsEnabled();

        Log.d("노티 가능성", "onCreate: " + ean);

        Intent intentnew = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intentnew);
//        Log.d("ena", "onCreate: "+ean);
        if (ean != true) {


        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }

        Button login_btn = (Button) findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText id = (EditText) findViewById(R.id.spinner_user);
                EditText pw = (EditText) findViewById(R.id.spinner_pass);

                id_content = id.getText().toString();
                pw_content = pw.getText().toString();

                if(id_content.equals("hong"))
                {
                    startActivity(new Intent(LoginActivity.this, tab_main.class));
                }


                JSONObject json = new JSONObject();

                TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) !=
                                PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();


                //Toast.makeText(LoginActivity.this,"get phone nmber"+mPhoneNumber,Toast.LENGTH_SHORT).show();

                try {
                    json.put("cmd",LOGIN_REQ);
                    json.put("agentId","");

                    json.put("id",id_content);
                    json.put("pw",pw_content);
                    json.put("phone",mPhoneNumber);

                    //Log.d("stryingfied josn is : ", json.toString());

                    mSocket.emit("login request to server",json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                SQLiteDatabase db = MemoDbHelper.getInstance(LoginActivity.this).getReadableDatabase();
                /*
                Cursor cursor = db.query(Call_Model.Callentry.TABLE_NAME,
                        new String[]{Call_Model.Callentry.USER_PW},null,null,null,null,null);

                String id = getString(cursor.getColumnIndexOrThrow("user_id"));
                String pw = getString(cursor.getColumnIndexOrThrow("user_pw"));



*/
                //startActivity(new Intent(LoginActivity.this, tab_main.class));


            }

        });

    }

    private  boolean checkAndRequestPermissions() {



        List<String> listPermissionsNeeded = new ArrayList<>();
        listPermissionsNeeded.clear();
        int recordaudio= ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);//
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);//
        int call= ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);//
        int read_phonestate= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);//
        int Capture_audio_output= ContextCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        int process_outgoing_call= ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS);//
        int modify_audio_setting= ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS);//
        int read_contacts= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);//
        int internet_permission= ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);//
        int manage_phone_permission= ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_OWN_CALLS);//

        //int read_concise_call_state= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PRECISE_PHONE_STATE);//



        if (read_contacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (manage_phone_permission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.MANAGE_OWN_CALLS);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (modify_audio_setting != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        }
        if (process_outgoing_call != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        }
        if (read_phonestate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (call != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (recordaudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (Capture_audio_output!=PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        }
        if (internet_permission != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LoginActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {




//                    socket_io_received_textview.setText(args[0].toString());

                    JSONObject data = (JSONObject) args[0];
                    String number;

                    String cmd= null;
                    try {
                        cmd = data.getString(CMD);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("cmd", "run: "+cmd);
                    switch (cmd)
                    {
                        case DIAL_REQ:

                            if(!is_login)
                            {
                                return;
                            }
                            try
                            {
                                number = data.getString("dial_number");
                                String tel = "tel:"+number;
                                startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));


                                //socket_io_received_textview.setText(number);





                            } catch (JSONException e)
                            {
                                try {
                                    send_ack("FAIL");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                return;
                            }
                            try {
                                send_ack("OK");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case DROP_REQ:


                            if(!is_login)
                            {
                                return;
                            }
                            try {
                                drop_call();
                            } catch (ClassNotFoundException e) {
                                try {
                                    send_drop_ack("FAIL");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                try {
                                    send_drop_ack("FAIL");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                try {
                                    send_drop_ack("FAIL");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                try {
                                    send_drop_ack("FAIL");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            }

                            try {
                                send_ack("OK");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;

                        case LOGIN_ACK:
                            String result = "";
                            String response_id = "";
                            try {
                                result = data.getString("result");
                                response_id = data.getString("id");
                                Log.d("server response",  result);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(result.equals("OK"))
                            {
                                //통과

                                Toast.makeText(LoginActivity.this,response_id+" Welcome!",Toast.LENGTH_SHORT).show();

                                is_login=true;
                                startActivity(new Intent(LoginActivity.this,tab_main.class));
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"try to correct id&pw plz",Toast.LENGTH_SHORT).show();

                            }

                            break;

                        case LOGOUT_ACK:
                            String logout_result = "";
                            String logout_id = "";

                            try
                            {
                                logout_result = data.getString("result");
                                logout_id = data.getString("id");
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            Log.d("Logout ACK", "result and id :" +logout_result+"  "+logout_id );

                            if(logout_result.equals("OK"))
                            {
                                //통과

                                Toast.makeText(LoginActivity.this,logout_id+" Bye Bye!",Toast.LENGTH_SHORT).show();
                                is_login=false;
                                startActivity(new Intent(LoginActivity.this,LoginActivity.class));



                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Id does not matched",Toast.LENGTH_SHORT).show();

                            }

                            break;



                        default:


                            break;




                    }







                    // 메시지를 받으면 data에 담고,
                    // username와 message라는 키값으로 들어왔다는 가정으로 작성된 코드다.
                    // addMessage(username, message); 이런 식으로 코드를 실행시키면 addMessage 쪽으로 인자를 담아 보내니 화면에 노출하게 만들면 될 것이다.

                }
            });
        }
    };

    public void send_ack(String result) throws JSONException {
        if( result.equals("OK"))
        {
            JSONObject json = new JSONObject();
            json.put(CMD,DIAL_ACK);
            json.put("result",result);
            mSocket.emit("send ack to server",json.toString());

        }
        else
        {
            JSONObject json = new JSONObject();
            json.put(CMD,DIAL_ACK);
            json.put("result","FAIL");
            mSocket.emit("send ack to server",json.toString());

        }

    }



    public void send_drop_ack(String result) throws JSONException {
        if( result.equals("OK"))
        {
            JSONObject json = new JSONObject();
            json.put(CMD,DROP_ACK);
            json.put("result",result);
            mSocket.emit("send ack to server",json.toString());

        }
        else
        {
            JSONObject json = new JSONObject();
            json.put(CMD,DROP_ACK);
            json.put("result","FAIL");
            mSocket.emit("send ack to server",json.toString());

        }

    }
    public void upload_data(JSONObject myobj)
    {
        mSocket.emit("upload data","hello");
    }
    public void drop_call() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        Method m1 = tm.getClass().getDeclaredMethod("getITelephony");
        m1.setAccessible(true);
        Object iTelephony = m1.invoke(tm);

        Method m2 = iTelephony.getClass().getDeclaredMethod("silenceRinger");
        Method m3 = iTelephony.getClass().getDeclaredMethod("endCall");

        //m2.invoke(iTelephony);
        m3.invoke(iTelephony);




    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0){

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please Allow All Permission To Continue..", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;

        }
    }

    public static int logout_request() throws JSONException
    {
        JSONObject json2 = new JSONObject();

            json2.put(CMD,LOGOUT_REQ);
            json2.put("id",id_content);
//            json2.put("agentId","");



            //Log.d("stryingfied josn is : ", json.toString());

            mSocket.emit("logout request to server",json2.toString());



            return 1;
    }
}
