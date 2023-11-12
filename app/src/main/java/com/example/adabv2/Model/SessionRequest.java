package com.example.adabv2.Model;

import com.google.gson.annotations.SerializedName;

public class SessionRequest {
    @SerializedName("user_secret")
    private String user_secret;
    @SerializedName("date")
    private String date;

    public void setUser_secret(String user_secret) {
        this.user_secret = user_secret;
    }

    public void setDate(String date) {
        this.date = date;
    }
}