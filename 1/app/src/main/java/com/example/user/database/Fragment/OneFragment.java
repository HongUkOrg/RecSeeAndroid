package com.example.user.database.Fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.database.Adapter.Call_Model;
import com.example.user.database.DB_UTIL.MemoDbHelper;
import com.example.user.database.MainActivity;
import com.example.user.database.R;
import com.example.user.database.Web_HTTP.UploadToServer;
import com.example.user.database.tab_main;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;



public class OneFragment extends ListFragment
{
    //intitalizing the string and images




    Button uploadButton;
    TextView messageText;

    String[] players={"Completely Uploaded File","Not Uploaded File"};
    int[] images={R.drawable.ic_local_search_airport_highlighted,R.drawable.ic_local_search_shipping_service_highlighted};

    //using arraylist and adapter


    ArrayList<HashMap<String, String>> data;
    SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_one, container, false);
        uploadButton = (Button) view.findViewById(R.id.uploadButton2);
        uploadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),UploadToServer.class);
                intent.putExtra("all","OK");

                startActivity(intent);

            }
        });

        data= new ArrayList<HashMap<String,String>>();

        //MAP
        HashMap<String, String> map=new HashMap<String, String>();


        String list1 = get_upload_list();
        String list2 = get_not_uploaded_list();

        String list[] = {list1,list2};

        //FILL
        for(int i=0;i<players.length;i++)
        {
            map=new HashMap<String, String>();
            map.put("Player", players[i]);
            map.put("Image", Integer.toString(images[i]));
            if(i==0)
            map.put("RecordList",list1);
            if(i==1)
            map.put("RecordList",list2);
            data.add(map);
        }
        //KEYS IN MAP
        String[] from={"Player","Image","RecordList"};
        //IDS OF VIEWS
        int[] to={R.id.nameTxt,R.id.imageView1,R.id.RecordList};
        //ADAPTER
        adapter=new SimpleAdapter(getActivity(), data, R.layout.model, from, to);
        setListAdapter(adapter);







        return view;
    }





    @Override
    public void onStart() {

        super.onStart();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id)
            {
                //display toast on click of item
                Toast.makeText(getActivity(), data.get(pos).get("Player"), Toast.LENGTH_SHORT).show();
            }
        });





    }

    public String get_upload_list()
    {

        String result="";
        MemoDbHelper dbHelper = MemoDbHelper.getInstance(getActivity());
        Cursor cursor;

        cursor = dbHelper.getReadableDatabase()
                .query(Call_Model.Callentry.TABLE_NAME, null,"upload='OK'",null,null,null,"_id DESC");



        while (cursor.moveToNext()) {


            result += "record_"+cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_START_TIME))+".mp3\n";




        }

        return result;





    }
    public String get_not_uploaded_list()
    {
        String result="";
        MemoDbHelper dbHelper = MemoDbHelper.getInstance(getActivity());
        Cursor cursor;

        cursor = dbHelper.getReadableDatabase()
                .query(Call_Model.Callentry.TABLE_NAME, null,"upload='NO' AND file_exist='OK'",null,null,null,"_id DESC");


        while (cursor.moveToNext()) {


            result += "record_"+cursor.getString(cursor.getColumnIndexOrThrow(Call_Model.Callentry.COLUMN_NAME_START_TIME))+".mp3\n";




        }

        return result;


    }
}
