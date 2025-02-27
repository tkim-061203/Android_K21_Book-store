package com.example.do_an.models;

public class Category {
    private String name;
    private String imageUrl;

    public Category() {
        // Firebase cần constructor rỗng
    }

    public Category(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}