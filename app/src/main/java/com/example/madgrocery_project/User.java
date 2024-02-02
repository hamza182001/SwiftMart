package com.example.madgrocery_project;

public class User {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String address;


    public User() {
    }

    public User(String userId, String username, String email, String password,String address) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address=address;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }
}
