package com.example.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String getMonth(Calendar calendar) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        return f.format(calendar.getTime());

    }

    public static String getNowDate(){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(new Date());

    }
    public static String getDate(Calendar calendar) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(calendar.getTime());

    }

    public static String getNowTimeDetail() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss.SSS");
        return f.format(new Date());

    }

    public static String getNowTime() {
        var f = new SimpleDateFormat("HH:mm:ss");
        return f.format(new Date());
    }

    public static Date formatString(String s) {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = f.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getNowDateTime(String str){
        SimpleDateFormat f ;
        if(TextUtils.isEmpty(str))f = new SimpleDateFormat("yyyyMMddHHmmss");
        else f = new SimpleDateFormat(str);
        return f.format(new Date());
    }

}
