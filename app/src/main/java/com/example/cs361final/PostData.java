package com.example.cs361final;

public class PostData {

    private String postOwnerUsername;
    private String postOwnerProfileImage;
    private String dateTime;
    private String content;
    private String postImage;

    // Constructor
    public PostData(String postOwnerUsername, String postOwnerProfileImage, String dateTime, String content, String postImage) {
        this.postOwnerUsername = postOwnerUsername;
        this.postOwnerProfileImage = postOwnerProfileImage;
        this.dateTime = dateTime;
        this.content = content;
        this.postImage = postImage;
    }

    // Getters
    public String getPostOwnerUsername() {
        return postOwnerUsername;
    }

    public String getPostOwnerProfileImage() {
        return postOwnerProfileImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getContent() {
        return content;
    }

    public String getPostImage() {
        return postImage;
    }

}
