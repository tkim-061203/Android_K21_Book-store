package com.example.do_an.ui_user;
public class Book {
    private String id;
    private String name;
    private String author;
    private String imageUrl;
    private Double price;
    private String category;
    private String description;

    // Constructor, getters, and setters
    public Book() { }

    public Book(String id, String name, String author, String imageUrl, Double price, String category, String description) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.imageUrl = imageUrl;
        this.price = price;
        this.category = category;
        this.description = description;
    }
    // Getters and setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
