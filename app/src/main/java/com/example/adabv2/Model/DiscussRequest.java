package com.example.adabv2.Model;

public class DiscussRequest {
    private String user_secret;
    private String query;

    public String getUser_secret() {
        return user_secret;
    }

    public void setUser_secret(String user_secret) {
        this.user_secret = user_secret;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
