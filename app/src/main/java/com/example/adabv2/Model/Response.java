package com.example.adabv2.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response<T> {
    @SerializedName("values")
    private List<T> values;

    public List<T> getValues() {
        return values;
    }
}
