package com.example.adabv2.Model;

public class LoginResponse {
    private int status;
    private ValueLoginResponse values;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ValueLoginResponse getValues() {
        return values;
    }

    public void setValues(ValueLoginResponse values) {
        this.values = values;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
