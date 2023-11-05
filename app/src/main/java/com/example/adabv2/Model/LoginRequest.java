package com.example.adabv2.Model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    private String user_email;
    private String user_password;

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
