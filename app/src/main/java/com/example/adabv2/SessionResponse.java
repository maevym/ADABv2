package com.example.adabv2;

import com.example.adabv2.Room.Session;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SessionResponse {
    @SerializedName("values")
    private List<Session> values;

    public List<Session> getValues() {
        return values;
    }
}
