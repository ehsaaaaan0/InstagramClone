package com.app.instagramclone.model;

public class PostModel {

    String username, likes, description;
    int profile, post;


    public PostModel(){

    }

    public PostModel(String username, String likes, String description, int profile, int post) {
        this.username = username;
        this.likes = likes;
        this.description = description;
        this.profile = profile;
        this.post = post;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }
}
