package com.example.do_an.ui_user;

public class Book {
    private String name;
    private String writer;
    private String imageUrl;
    private float rating;

    public Book(String name, String writer, String imageUrl, float rating) {
        this.name = name;
        this.writer = writer;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getWriter() { return writer; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
}