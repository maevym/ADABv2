package com.example.adabv2.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class ClassSession {

    @PrimaryKey(autoGenerate = true)
    private int session_id;
    @ColumnInfo(name = "class_id")
    private int class_id;
    @ColumnInfo(name = "session_start")
    private String session_start;
    @ColumnInfo(name = "session_end")
    private String session_end;
    @ColumnInfo(name = "session_name")
    private String session_name;
    @ColumnInfo(name = "time_start")
    private String time_start;
    @ColumnInfo(name = "time_end")
    private String time_end;
    @ColumnInfo(name = "session_location")
    private String session_location;

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getSession_start() {
        return session_start;
    }

    public void setSession_start(String session_start) {
        this.session_start = session_start;
    }

    public String getSession_end() {
        return session_end;
    }

    public void setSession_end(String session_end) {
        this.session_end = session_end;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getSession_location() {
        return session_location;
    }

    public void setSession_location(String session_location) {
        this.session_location = session_location;
    }
}
