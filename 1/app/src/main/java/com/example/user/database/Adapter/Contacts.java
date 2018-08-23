package com.example.user.database.Adapter;

import android.graphics.Bitmap;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import java.util.Date;

public class Contacts implements Comparable,BaseColumns {
    int id;
    int view;
    String name;
    String number;
    Bitmap photo;
    String photoUri;
    String time;
    int fav;
    int state;
    String date;
    String records;
    Date start;
    Date end;
    String in_or_out;
    String miss_reason;
    String voice_record;
    String is_uploaded;
    private String info_posted;

    public Date getStart()
    {
        return start;

    }
    public Date getEnd()
    {
        return end;

    }

    public void setInfo_posted(String info_posted) {
        this.info_posted = info_posted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Contacts(){

    }
    public Contacts(String name, String _phone_number, Date start,
                    Date end,String in_or_out,String miss_reason,String voice_record,String upload,String info_posted)
    {
        this.name = name;
        this.number = _phone_number;
        this.end=end;
        this.start=start;
        this.in_or_out=in_or_out;
        this.miss_reason = miss_reason;
        this.voice_record=voice_record;
        this.is_uploaded=upload;
        this.info_posted=info_posted;

    }

    public String getIn_or_out()
    {
        return in_or_out;

    }


    public void setIs_uploaded(String is_uploaded)
    {
        this.is_uploaded=is_uploaded;
    }

    public String getIs_uploaded()
    {
        return this.is_uploaded;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public int getId() {
        return id;
    }

    public String getVoiceRecord() {
        return voice_record;
    }

    public void setVoiceRecord(String voice_record) {
        this.voice_record = voice_record;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {

        return time;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getFav() {
        return fav;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int compareage= (int) ((Contacts)o).getTimestamp();
        /* For Ascending order*/
        return (int) (this.timestamp-compareage);

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }

    public String get_miss_reason() {
        return miss_reason;
    }

    public String get_info_posted() {
        return this.info_posted;
    }
}
