package com.example.user.database;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.database.Adapter.SettingRecyclerAdapter;
import com.example.user.database.base.PinnedHeaderItemDecoration;
import com.example.user.database.model.SettingItem;

import java.util.List;

import com.example.user.database.R;

import org.json.JSONException;

/**
 * Setting Activity
 * reference:https://github.com/sockeqwe/AdapterDelegates
 * and
 * https://github.com/takahr/pinned-section-item-decoration
 *
 * @author Lshare
 * @date 2016/11/2
 */
public class SettingActivity extends AppCompatActivity
        implements SettingRecyclerAdapter.OnItemClickListener {

    public static boolean wifi_upload_checked = false;

    RecyclerView recyclerView;
    public static SettingRecyclerAdapter settingRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        TextView os_text = (TextView) findViewById(R.id.os_info);
        TextView device_info_text = (TextView) findViewById(R.id.device_info);
        final TextView wifi_state = (TextView) findViewById(R.id.wifi_state);
        TextView phone_number_text = (TextView) findViewById(R.id.phone_number_text);


        os_text.setText(Build.VERSION.RELEASE);
        device_info_text.setText(Build.MODEL);
        phone_number_text.setText(LoginActivity.Phone_number);

        update_wifi_state(wifi_state);


        Switch wifi_upload_switch = (Switch) findViewById(R.id.wifi_upload_switch);

        wifi_upload_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    Toast.makeText(SettingActivity.this, "check!", Toast.LENGTH_SHORT).show();
                    wifi_upload_checked = true;
                } else {
                    Toast.makeText(SettingActivity.this, "UnChecked!", Toast.LENGTH_SHORT).show();
                    wifi_upload_checked = false;
                }


            }
        });


        wifi_state.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view)
            {
                update_wifi_state(wifi_state);

            }
        });


        RelativeLayout log_rel = (RelativeLayout) findViewById(R.id.logout_row);

        log_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    LoginActivity.logout_request();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


//    init();
    }

    private void update_wifi_state(TextView wifi_state) {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected())
        {

            wifi_state.setText("현재 연결 됨");
            // Do whatever
        } else {
            wifi_state.setText("연결되지 않음");
        }
    }
//
//  private void init() {
//    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//
//    recyclerView.setLayoutManager(
//        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//    settingRecyclerAdapter = new SettingRecyclerAdapter();
//    recyclerView.setAdapter(settingRecyclerAdapter);
//    recyclerView.addItemDecoration(new PinnedHeaderItemDecoration());
//
//    settingRecyclerAdapter.setOnItemClickListener(this);
//
//    List<SettingItem> settingItemList = Util.parseSettings(getResources(), R.xml.settings);
//    settingRecyclerAdapter.addAll(settingItemList);
//
//
//  }


    @Override
    public void onItemClick(int id, SettingItem item) throws JSONException {


        if (id == R.id.name) {
            settingRecyclerAdapter.updateSecondaryText(id, "Here it go");
        }

        if (id == R.id.logout) {

            LoginActivity.logout_request();


        } else {
//      Util.toast(this, item);
            Toast.makeText(SettingActivity.this, "id" + id, Toast.LENGTH_SHORT).show();
        }

    }
}