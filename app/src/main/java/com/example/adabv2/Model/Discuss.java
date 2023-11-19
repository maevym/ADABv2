package com.example.adabv2.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Discuss {
    @PrimaryKey(autoGenerate = true)
    private int user_id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "user_unique")
    private String user_unique;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_unique() {
        return user_unique;
    }

    public void setUser_unique(String user_unique) {
        this.user_unique = user_unique;
    }
}
