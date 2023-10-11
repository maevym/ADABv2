package com.example.adabv2.Model;

public class ValueLoginResponse {
    private String user_secret;
    private String name;
    private String user_type;

    public String getUser_secret() {
        return user_secret;
    }

    public void setUser_secret(String user_secret) {
        this.user_secret = user_secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
