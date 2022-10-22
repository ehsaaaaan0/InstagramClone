package com.app.instagramclone.model;

public class CommentModel {
    String comment, id;
    long time;

    public CommentModel() {

    }

    public CommentModel(String comment, String id, long time) {
        this.comment = comment;
        this.id = id;
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
