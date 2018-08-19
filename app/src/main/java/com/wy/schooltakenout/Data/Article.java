package com.wy.schooltakenout.Data;

public class Article {
    private String title;
    private String content;
    private int imageId;
    private String time;

    public Article(String title, String content, int imageId, String time) {
        this.title = title;
        this.content = content;
        this.imageId = imageId;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTime() {
        return time;
    }
}
