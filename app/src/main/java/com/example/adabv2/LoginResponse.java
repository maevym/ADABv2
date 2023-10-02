package com.example.adabv2;

import com.example.adabv2.Room.Login;
import com.example.adabv2.Room.Session;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {
    @SerializedName("values")
    private List<Login> values;

    public List<Login> getValues() {
        return values;
    }

}
