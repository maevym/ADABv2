package com.example.adabv2;

public class DataUser {
    private String username;
    private String email;
    private String password;
    private String role;
    private String nim;

    public DataUser(String username, String email, String password, String role, String nim) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nim = nim;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }
}
