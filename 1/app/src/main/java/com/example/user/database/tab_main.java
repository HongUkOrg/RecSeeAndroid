package com.example.user.database;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

public class tab_main extends TabActivity {

    TabHost tabHost;

    private int currentTab=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_info);
        final TabHost tabHost = getTabHost();


//        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));



        addTab("Call",getResources().getDrawable(R.drawable.icon_menu_call_over),MainActivity.class);

        addTab("History",getResources().getDrawable(R.drawable.icon_menu_history),IconTabsActivity.class);
        //addTab("File",getResources().getDrawable(R.drawable.icon_menu_history),UploadToServer.class);
        addTab("Setting",
                new IconicsDrawable(this)
                        .icon(FontAwesome.Icon.faw_cog)
                        .color(Color.rgb(148,148,148))
                        .sizeDp(24),SettingActivity.class);







        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId)
            {




                int menu_off[] = {R.drawable.icon_menu_call_over,
                        R.drawable.icon_menu_history_over,R.drawable.icon_menu_file_over,R.drawable.icon_menu_user_over};


                int menu_on[] = {R.drawable.icon_menu_call,
                       R.drawable.icon_menu_history, R.drawable.icon_menu_file,R.drawable.icon_menu_user};

                for(int i=0; i<getTabHost().getTabWidget().getChildCount();i++)
                {


                    View view = getTabHost().getTabWidget().getChildAt(i);

                    ImageView iv;
                    iv=(ImageView)view.findViewById(R.id.icon);
                    // iv = (ImageView)getTabHost().getTabWidget().getChildAt(i).findViewById(R.id.icon);
                    if(i==2) {
                        iv.setImageDrawable(new IconicsDrawable(tab_main.this)
                                .icon(FontAwesome.Icon.faw_cog)
                                .color(Color.rgb(148, 148, 148))
                                .sizeDp(24));


                    }
                    else {
                        iv.setImageDrawable(getResources().getDrawable(menu_on[i]));
                    }

                }

                ImageView ip;
                View view=getTabHost().getTabWidget().getChildAt(getTabHost().getCurrentTab());
                ip=(ImageView)view.findViewById(R.id.icon);
                //ip=(ImageView)getTabHost().getTabWidget().getChildAt(getTabHost().getCurrentTab()).findViewById(R.id.icon);
                if(getTabHost().getCurrentTab()==2)
                {

                    ip.setImageDrawable(new IconicsDrawable(tab_main.this)
                            .icon(FontAwesome.Icon.faw_cog)
                            .color(Color.rgb(00, 141, 196))
                            .sizeDp(24));



                }
                else
                {
                ip.setImageDrawable(getResources().getDrawable(menu_off[getTabHost().getCurrentTab()]));

                }




            }

        });





    }



    private void addTab(String labelId, Drawable drawable, Class<?> c) {

        TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);


        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.top_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);

        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageDrawable(drawable);
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        spec.setContent(intent);
        spec.setIndicator(tabIndicator);

        tabHost.addTab(spec);
    }






}
