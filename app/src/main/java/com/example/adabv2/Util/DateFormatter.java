package com.example.adabv2.Util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static String DateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    public static Date StringToDateMillisecond(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            Log.wtf("error", e);
            return new Date();
        }
    }

    public static Date StringToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            Log.wtf("error", e);
            return new Date();
        }
    }

    public static String DateToTime(Date date) {
        return new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(date);
    }

    public static String DateToStringDate(Date date) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
    }

    public static String DateToStringChat(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(date);
    }
}

