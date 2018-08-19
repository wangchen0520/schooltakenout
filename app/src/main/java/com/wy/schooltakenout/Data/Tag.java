package com.wy.schooltakenout.Data;

public class Tag {
    private String name;
    private int imageId;

    public Tag(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
