package com.example.adabv2.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity
public class Login {
    @ColumnInfo(name = "user_secret")
    @SerializedName("user_secret")
    public String user_secret;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String name;

}
