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
    public int sessionID;
    @ColumnInfo(name = "class_id")
    @SerializedName("class_id")
    public int classID;
    @ColumnInfo(name = "session_location")
    @SerializedName("session_location")
    public String sessionLocation;
    @ColumnInfo(name = "sessionstrt")
    @SerializedName("sessionstrt")
    public String sessionStart;
    @ColumnInfo(name = "session_end")
    @SerializedName("session_end")
    public String sessionEnd;
    @ColumnInfo(name = "session_name")
    @SerializedName("session_name")
    public String sessionName;
    @ColumnInfo(name = "class_name")
    @SerializedName("class_name")
    public String className;
    @ColumnInfo(name = "class_code")
    @SerializedName("class_code")
    public String classCode;

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
