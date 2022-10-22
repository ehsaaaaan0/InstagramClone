package com.app.instagramclone.model;

public class PostsModel {
    String id, title, desciption, image, key,savedPost;
    long postAt;
    int likes;


    public PostsModel(){

    }


    public PostsModel(String id, String title, String desciption, String image, long postAt, int likes, String key) {
        this.id = id;
        this.title = title;
        this.desciption = desciption;
        this.image = image;
        this.postAt = postAt;
        this.likes = likes;
        this.key = key;
    }

    public String getSavedPost() {
        return savedPost;
    }

    public void setSavedPost(String savedPost) {
        this.savedPost = savedPost;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getPostAt() {
        return postAt;
    }

    public void setPostAt(long postAt) {
        this.postAt = postAt;
    }
}
