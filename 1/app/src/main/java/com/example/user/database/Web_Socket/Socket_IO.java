package com.example.user.database.Web_Socket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.database.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

public class Socket_IO extends AppCompatActivity {

    EditText socket_io_edit_text;
    Button socket_io_send_button;
    TextView socket_io_received_textview;


    public static final String CMD = "cmd";
    public static final String DIAL_REQ = "dial_mobile_req";
    public static final String DIAL_ACK="dial_mobile_ack";
    public static final String DROP_REQ ="drop_mobile_req";
    public static final String DROP_ACK = "drop_mobile_ack";


    private Socket mSocket;
    {
        try
        {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket__io);

        mSocket.on("call to customer",onNewMessage);
        mSocket.connect();

        socket_io_edit_text = (EditText)findViewById(R.id.socket_io_edit_text);

        socket_io_send_button = (Button)findViewById(R.id.btn_socket_io_send);
        socket_io_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                //data에 db관련 내용을 보낼 수 있음.
                String data = socket_io_edit_text.getText().toString();
                mSocket.emit("update customer",data);


            }
        });
    }







    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Socket_IO.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {



                    socket_io_received_textview=(TextView)findViewById(R.id.socket_io_received_message);
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
                            try
                            {
                                number = data.getString("dial_number");
                                String tel = "tel:"+number;
                                //startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));


                                //socket_io_received_textview.setText(number);

                                drop_call();



                            } catch (JSONException e)
                            {
                                try {
                                    send_ack("FAIL");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                return;
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            try {
                                send_ack("OK");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case DROP_REQ:
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

    public void answer_call() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        Method m1 = tm.getClass().getDeclaredMethod("getITelephony");
        m1.setAccessible(true);
        Object iTelephony = m1.invoke(tm);

        Method m2 = iTelephony.getClass().getDeclaredMethod("silenceRinger");
        Method m3 = iTelephony.getClass().getDeclaredMethod("answerRingingCall");

        //m2.invoke(iTelephony);
        m3.invoke(iTelephony);




    }
}
