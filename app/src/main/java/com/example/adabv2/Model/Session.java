package com.example.adabv2.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity
public class Session {
    @PrimaryKey
    @ColumnInfo(name = "session_id")
    @SerializedName("session_id")
    private int sessionID;
    @ColumnInfo(name = "class_id")
    @SerializedName("class_id")
    private int classID;
    @ColumnInfo(name = "session_location")
    @SerializedName("session_location")
    private String sessionLocation;
    @ColumnInfo(name = "sessionstrt")
    @SerializedName("sessionstrt")
    private String sessionStart;
    @ColumnInfo(name = "session_end")
    @SerializedName("session_end")
    private String sessionEnd;
    @ColumnInfo(name = "session_name")
    @SerializedName("session_name")
    private String sessionName;
    @ColumnInfo(name = "class_name")
    @SerializedName("class_name")
    private String className;
    @ColumnInfo(name = "class_code")
    @SerializedName("class_code")
    private String classCode;

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getSessionLocation() {
        return sessionLocation;
    }

    public void setSessionLocation(String sessionLocation) {
        this.sessionLocation = sessionLocation;
    }

    public String getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(String sessionStart) {
        this.sessionStart = sessionStart;
    }

    public String getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(String sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
