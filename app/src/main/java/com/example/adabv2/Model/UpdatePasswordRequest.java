package com.example.adabv2.Model;

import com.example.adabv2.UserPreferences;

public class UpdatePasswordRequest {
    private String user_secret;
    private String user_oldPassword;
    private String user_newPassword;

    public void setUser_secret(String user_secret) {
        this.user_secret = user_secret;
    }

    public void setUser_oldPassword(String user_oldPassword) {
        this.user_oldPassword = user_oldPassword;
    }

    public void setUser_newPassword(String user_newPassword) {
        this.user_newPassword = user_newPassword;
    }
}
