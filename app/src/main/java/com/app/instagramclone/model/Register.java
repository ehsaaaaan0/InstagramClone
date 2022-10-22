package com.app.instagramclone.model;

public class Register {
    String email_phone, full_name, password, dob, username, id;
    String Image, bio;
    int followerscount, followingcount;

    public int getFollowingcount() {
        return followingcount;
    }

    public void setFollowingcount(int followingcount) {
        this.followingcount = followingcount;
    }

    //Empty Constructor
    public Register(){

    }

    public Register(String email_phone, String full_name, String password, String dob, String username, String id, String image, String bio, int followercount, int followingcount) {
        this.email_phone = email_phone;
        this.full_name = full_name;
        this.password = password;
        this.dob = dob;
        this.username = username;
        this.id = id;
        Image = image;
        this.bio = bio;
        this.followerscount = followercount;
        this.followingcount = followingcount;
    }

    public Register(String email_phone, String full_name, String password, String dob, String username, String id, int followercount, int followingcount) {
        this.email_phone = email_phone;
        this.full_name = full_name;
        this.password = password;
        this.dob = dob;
        this.username = username;
        this.id = id;
        this.followerscount = followercount;
        this.followingcount = followingcount;
    }

    //Register Constructor
    public Register(String email_phone, String full_name, String password, String dob, String username, String id) {
        this.email_phone = email_phone;
        this.full_name = full_name;
        this.password = password;
        this.dob = dob;
        this.username = username;
        this.id= id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail_phone() {
        return email_phone;
    }

    public void setEmail_phone(String email_phone) {
        this.email_phone = email_phone;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getFollowerscount() {
        return followerscount;
    }

    public void setFollowercount(int followercount) {
        this.followerscount = followercount;
    }
}
