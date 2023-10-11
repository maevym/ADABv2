package com.example.adabv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserPreferences {
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_SECRET= "user_secret";
    private static final String KEY_USER_TYPE = "user_type";
    private static Context context = null;

    public UserPreferences(Context context) {
        this.context = context;
    }

    private static SharedPreferences getSharedPreference() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUserName(String userName){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() { return getSharedPreference().getString(KEY_USER_NAME,""); }

    public void setUserSecret(String userSecret){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(KEY_USER_SECRET, userSecret);
        editor.apply();
    }

    public String getUserSecret(){
        return getSharedPreference().getString(KEY_USER_SECRET,"");
    }

    public void setUserType(String usertoken){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(KEY_USER_TYPE, usertoken);
        editor.apply();
    }

    public String getUserType(){
        return getSharedPreference().getString(KEY_USER_TYPE,"");
    }
}
