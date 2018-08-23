package com.example.user.database.Receiver;

import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class EchoWebSocketListner extends WebSocketListener
{

    public static final int NORMAL_CLOSURE_STATUS = 1000;
    private static final String TAG = "Web_Socket_Listener : " ;

    @Override
    public void onOpen(WebSocket webSocket, Response response)
    {
//        super.onOpen(webSocket, response);
        webSocket.send("hello,it's SSaurel !");
        webSocket.send("what's up?");
        webSocket.send(ByteString.decodeHex("deadbeef"));
        webSocket.close(NORMAL_CLOSURE_STATUS,"GoodBye!");





    }

    @Override
    public void onMessage(WebSocket webSocket, String text)
    {




//        super.onMessage(webSocket, text);

        output("Receiving : "+text);


    }

    private void output(String s)
    {
        Log.d(TAG, "output: "+s);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes)
    {
//        super.onMessage(webSocket, bytes);

        output("Receiving bytes :  "+bytes.hex());

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason)
    {
//        super.onClosing(webSocket, code, reason);

    webSocket.close(NORMAL_CLOSURE_STATUS,null);
    output("Closing :"+code+"/"+reason );


    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response)
    {

        output("Error : "+t.getMessage());
    }
}
