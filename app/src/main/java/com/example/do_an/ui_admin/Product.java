package com.example.do_an.ui_admin;

public class Product {
    private String id;
    private String name;
    private String author;  // New field for author
    private String category;
    private String imageUrl;
    private double price;

    // No-argument constructor required for Firestore deserialization
    public Product() {
    }

    // Constructor with parameters including author
    public Product(String id, String name, String author, String category, String imageUrl, double price) {
        this.id = id;
        this.name = name;
        this.author = author;  // Set author
        this.category = category;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    // Getters and setters for each field
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

}
