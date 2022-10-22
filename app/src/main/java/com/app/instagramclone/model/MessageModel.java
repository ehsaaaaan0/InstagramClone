package com.app.instagramclone.model;

public class MessageModel {
    String id, message, image;
    long timestamp;

    public MessageModel(){}

    public MessageModel(String id, String message, String image) {
        this.id = id;
        this.message = message;
        this.image = image;
    }

    public MessageModel(String id, String message, long timestamp) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
