package com.example.do_an.ui_admin;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {

    private String id;
    private String name;
    private double price;
    private String imageUrl;
    private String description;
    private String category;
    private String author;  // New field for author

    // Default constructor required for Firestore
    public Product() {
    }

    // Constructor to initialize the Product object
    public Product(String id, String name, double price, String imageUrl, String description, String category, String author) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.category = category;
        this.author = author;  // Initialize author
    }

    // Getters and Setters
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
