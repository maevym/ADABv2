package com.example.adabv2.Model;

public class Chat {
    private String username;
    private String message;
    private String time;
    private String roomId;

    public Chat(String username, String message, String time, String roomId) {
        this.username = username;
        this.message = message;
        this.time = time;
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
