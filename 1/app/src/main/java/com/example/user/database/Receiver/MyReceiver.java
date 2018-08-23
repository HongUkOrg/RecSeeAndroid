package com.example.user.database.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.telecom.DisconnectCause;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.user.database.Adapter.Call_Model;
import com.example.user.database.Adapter.Contacts;
import com.example.user.database.MainActivity;
import com.example.user.database.DB_UTIL.MemoDbHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by sandhya on 22-Aug-17.
 */


public class MyReceiver extends BroadcastReceiver {


    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    private static MediaRecorder recorder;
    public Thread thread;
    static AudioManager audioManager;
    static File audiofile;
    Context context;
    public static boolean record = false;
    private String TAG = this.getClass().getSimpleName();
    public static boolean outgoing_missed = false;
    private Myreceiver2 NotiReceiver;
    MainActivity main;
    private DisconnectCause dis;
    private TelephonyManager mTel;




    public MyReceiver() {
        super();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        //Call call;

        //CharSequence a = dis.getDescription().toString();
        //Log.d("disconnect method : ", "onReceive: "+a);



        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {


            //Toast.makeText(context,"outgoing_call_start",Toast.LENGTH_SHORT).show();

            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            //System.out.println(savedNumber);


        } else if (intent.getAction().equals(TelecomManager.ACTION_SHOW_MISSED_CALLS_NOTIFICATION))

        {
            mTel = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);


//            Log.d("calls noti", "calls noti implemeneted: ");
        } else

        {


            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);

            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            int state = 0;

            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, number);
        }
    }



    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        Toast.makeText(ctx, "Incoming Call_Deteced", Toast.LENGTH_SHORT).show();
        TelecomManager tm = (TelecomManager) ctx.getSystemService(Context.TELECOM_SERVICE);


//        Uri uri2 = Uri.fromParts("tel", "12345", null);
//        Bundle extras2 = new Bundle();
//        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true);
//        tm.placeCall(uri, extras);

        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
//            final List<PhoneAccountHandle> enabledAccounts = tm.getCallCapablePhoneAccounts();
            PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(
                    new ComponentName(ctx.getApplicationContext(), MyReceiver3.class),
                    "example");

//            PhoneAccount ac = getPhoneAccount(phoneAccountHandle);
//            PhoneAccountHandle han = enabledAccounts.get(0);

            PhoneAccount phoneAccount = PhoneAccount.builder(phoneAccountHandle, "example").setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER).build();


            Bundle extras = new Bundle();
            Uri uri = Uri.fromParts(PhoneAccount.SCHEME_TEL, "010-2043-8751", null);
            extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, uri);
            extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
            tm.addNewIncomingCall(phoneAccountHandle, extras);








//            if(!enabledAccounts.isEmpty()) {
//                PhoneAccount acc = tm.getPhoneAccount(enabledAccounts.get(0));
//                tm.registerPhoneAccount(acc);
//                {

//                }





        }
        */

    }





    public void onOutgoingCallStarted(Context ctx, String number, Date start)
    {

        if(!record) startRecord(start);
        //NotiReceiver.onListenerConnected();
        //Toast.makeText(ctx,"Outgoing call_started!",Toast.LENGTH_SHORT).show();
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        MemoDbHelper dbHelper = MemoDbHelper.getInstance(ctx);
        dbHelper.save_Contact(new Contacts("hongcha",number,start,end,"IN","","OK","NO","NO"));
        //main = main.set_context(ctx);

        stopRecording();
        record=false;
        main.updateCursor(ctx,getChangedCursor(ctx));

    }

    public void onOutgoingCallEnded(Context ctx, String number, Date start, Date end,String state)
    {
        if(state.equals("OUT MISS"))

        {
            Intent i = new Intent(ctx, AlertDialogActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("start", start);
            i.putExtra("name", "HongUk");
            i.putExtra("phone", number);
            i.putExtra("state",state);
            context.startActivity(i);
        }

        else {

            MemoDbHelper dbHelper = MemoDbHelper.getInstance(ctx);
            dbHelper.save_Contact(new Contacts("hongcha", number, start, end, state,"","OK","NO","NO"));
            //main=main.set_context(ctx);
            main.updateCursor(ctx, getChangedCursor(ctx));
            stopRecording();


            record = false;
        }



    }

    protected void onMissedCall(Context ctx, String number, Date start,String state)
    {
        Intent i = new Intent(ctx, AlertDialogActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("start", start);
        i.putExtra("name", "HongUk");
        i.putExtra("phone", number);
        i.putExtra("state",state);
        context.startActivity(i);
//        Toast.makeText(ctx,"Call_Missed, Reason : quit",Toast.LENGTH_SHORT).show();

//
//        MemoDbHelper dbHelper = MemoDbHelper.getInstance(ctx);
//
//        dbHelper.save_Contact(new Contacts("hongcha", number, start, start, "IN MISS",""));
//        main.updateCursor(ctx,getChangedCursor(ctx));


    }

    protected void onIncomingCallAnswered(Context context, String savedNumber, Date callStartTime)
    {
        if(!record)
        startRecord(callStartTime);
    }


    public Cursor getChangedCursor(Context ctx)
    {

        MemoDbHelper dbHelper = MemoDbHelper.getInstance(ctx);
        return dbHelper.getReadableDatabase()
                .query(Call_Model.Callentry.TABLE_NAME,
                        null,null,null,null,null,"_id DESC");

    }

    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            //No change
            return;
        }

        switch (state) {

            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (lastState != TelephonyManager.CALL_STATE_RINGING)
                {
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);

                }
                else {
                    isIncoming = true;
                    callStartTime = new Date();
                    onIncomingCallAnswered(context, savedNumber, callStartTime);
                }

                break;

            case TelephonyManager.CALL_STATE_IDLE:
                //call ended
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    // a miss call
                    onMissedCall(context, savedNumber, callStartTime,"IN MISS");
                }
                else if (isIncoming)
                {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    isIncoming = false;
                }
                else if(outgoing_missed)
                {
                    onOutgoingCallEnded(context,savedNumber,callStartTime,new Date(),"OUT MISS");
                    outgoing_missed = false;

                }

                else { //ringing 도 아니고, inComing도 아니다. 즉, Outgoing 이고,  상태가 끝남
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date(),"OUT");
                }
                break;
        }
        lastState = state;
    }
    public void missed_call_toggle_true()
    {

        outgoing_missed = true;


    }
    public void missed_call_toggle_false()
    {

        outgoing_missed = true;


    }










    public  void startRecord(Date start){

        recorder=new MediaRecorder();
//        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
//        int source= Integer.parseInt(SP.getString("RECORDER","2"));


        File storeDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyRecSee");
        if (!storeDir.exists()) {
            if (!storeDir.mkdirs()) {
//                Log.d("RecSee", "failed to create directory");
                return;
            }
        }




        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh:mm:ss");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String start_time = dateFormat.format(start);

        String child;


        child = "record_";

        String extension = ".mp3";

        child += start_time;
        child += extension;
        audiofile = new File(storeDir.getAbsolutePath(), child);
//        Log.d(TAG, "file_record: "+audiofile.getAbsolutePath());
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.reset();
        try
        {
//
//            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//               recorder.setOutputFile(file.getAbsolutePath()+"/recsee"+new Date()+".m4a");
//            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);






            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setAudioSamplingRate(44100);
            recorder.setAudioEncodingBitRate(96000);
            recorder.prepare();



            //audioManager =(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            //audioManager.setStreamVolume(3,audioManager.getStreamMaxVolume(3),0);
            //audioManager.setSpeakerphoneOn(true);



        }catch (Exception e)
        {
            e.printStackTrace();
        }


        try {

            recorder.start();
            record = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e9){
            e9.printStackTrace();
        }catch (Exception e2){
            e2.printStackTrace();
        }
    }


    public void stopRecording() {
        if (record)
        {
            try {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder=null;

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(audioManager!=null){
            audioManager.setSpeakerphoneOn(false);
        }
    }






}


