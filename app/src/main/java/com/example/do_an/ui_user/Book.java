package com.example.do_an.ui_user;
public class Book {
    private String id;
    private String name;
    private String author;
    private String imageUrl;
    private Double price;

    // Constructor, getters, and setters
    public Book() { }

    public Book(String id, String name, String author, String imageUrl, Double  price) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double  price) {
        this.price = price;
    }
}
