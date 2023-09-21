package com.example.adabv2.Util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    public static String DateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        return  dateFormat.format(date);
    }

    public static Date StringToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            Log.wtf("error", e);
            return new Date();
        }
    }
}

