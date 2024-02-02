package com.example.madgrocery_project;

public class NotificationModel {
    String message;

    public NotificationModel(String message) {
        this.message = message;
    }

    public NotificationModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
