package com.example.user.database;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.support.annotation.XmlRes;
import android.util.Log;
import android.widget.Toast;

import com.example.user.database.model.SettingItem;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author Lshare
 * @date 2016/11/29
 * <p>
 * Copyright (c) 2016. WUDE All rights reserved.
 */
public class Util
{

  private static Gson singleGson;

  static {
    singleGson = new Gson();
  }

  public static String toString(Object object) {
    if (object == null) {
      return "";
    }
    String json = singleGson.toJson(object);
    return json;
  }

  public static void toast(Context context, Object object) {
    Toast.makeText(context, "" + toString(object), Toast.LENGTH_SHORT).show();
  }

  /**
   * get resource id from resName and Class Type
   */
  public static int getResId(String resName, Class<?> c)
  {
    try
    {
//      Log.d("Seeting inspection", "getResId: " + resName);
//      Log.d("Seeting inspection", "getResId: " + c);

      int result = -1;

      switch (resName)
      {
        case "auto" :

          result =  R.drawable.icon_hong_0;
          break;
        case "bit" :
          result =  R.drawable.icon_hong_1;
          break;
        case "foramt":
          result = R.drawable.icon_hong_2;
          break;
          default:
            break;



      }

      return result;
//      Field idField = c.getDeclaredField(resName);
//      return -1;
     // return getResources().getDrawable(R.drawable.icon_menu_history)
    } catch (Exception e)
    {
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * parse xml to a SettingItem List
   */
  public static List<SettingItem> parseSettings(Resources resources, @XmlRes int xmlRes) {
    XmlResourceParser xmlResourceParser = resources.getXml(xmlRes);
    List<SettingItem> settingItemList = null;
    SettingItem.Builder settingItemBuilder = null;
    try {
      int eventType = xmlResourceParser.getEventType();
      while (eventType != XmlPullParser.END_DOCUMENT)
      {
        switch (eventType) {
          case XmlPullParser.START_DOCUMENT:
            settingItemList = new ArrayList<>();
            break;
          case XmlPullParser.START_TAG:
            String name = xmlResourceParser.getName();
            String mainTxt = xmlResourceParser.getAttributeValue(null, "name");
//            Log.d("Util left Icon value", "Maintext: " + mainTxt);
            int id = xmlResourceParser.getAttributeResourceValue(
                "http://schemas.android.com/apk/res/android", "id", 0);
            if (name.equals("header"))
            {
              settingItemBuilder = new SettingItem.Builder(id, mainTxt, true);
            }
            else if (name.equals("item"))
            {

              settingItemBuilder = new SettingItem.Builder(id, mainTxt);
              String secondaryTxt = xmlResourceParser.getAttributeValue(null, "secondaryTxt");

//              Log.d("Util left Icon value", "Secondarytext: " + secondaryTxt);

              boolean hightLight =
                  xmlResourceParser.getAttributeBooleanValue(null, "hightLight", false);

              settingItemBuilder.secondaryText(secondaryTxt, hightLight);

              boolean showRightIcon =
                  xmlResourceParser.getAttributeBooleanValue(null, "showRightIcon", true);

//              Log.d("Util left Icon value", "right Icon: " + showRightIcon);

              if (!showRightIcon)
              {
                settingItemBuilder.hideRightIcon();
              }

              String showSwitch = xmlResourceParser.getAttributeValue(null, "showSwitch");
              String switch_default = xmlResourceParser.getAttributeValue(null, "default");



//              Log.d("Util left Icon value", "showswitch: " + showSwitch);

              if (showSwitch != null)
              {
                settingItemBuilder.showSwitch(showSwitch.equals("on"),switch_default.equals("on"));
              }




              String leftIconRes = xmlResourceParser.getAttributeValue(null, "leftIconRes");

//              Log.d("Util left Icon value", "left icon value : " + leftIconRes);

              if(leftIconRes!=null)
                settingItemBuilder.leftIconRes(Util.getResId(leftIconRes, Drawable.class));
            }
            break;
          case XmlPullParser.END_TAG:
            if (xmlResourceParser.getName().equals("header") || xmlResourceParser.getName()
                .equals("item"))
            {
              settingItemList.add(settingItemBuilder.build());
            }
            break;
        }
        eventType = xmlResourceParser.next();
      }
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return settingItemList;
  }
}
