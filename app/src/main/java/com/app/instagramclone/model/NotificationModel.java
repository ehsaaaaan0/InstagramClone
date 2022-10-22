package com.app.instagramclone.model;

public class NotificationModel {
    String user, notification,key,followedBy, image;
    int profile;
    long time;

    public NotificationModel() {

    }



    public NotificationModel(String user, String notitext, long time, String key, String image) {
        this.user = user;
        this.notification = notitext;
        this.time = time;
        this.key = key;
        this.image = image;
    }

    public NotificationModel(String user, String notification, long time, String key) {
        this.user = user;
        this.notification = notification;
        this.time = time;
        this.key = key;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(String followedBy) {
        this.followedBy = followedBy;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNotitext() {
        return notification;
    }

    public void setNotitext(String notitext) {
        this.notification = notitext;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }
}
