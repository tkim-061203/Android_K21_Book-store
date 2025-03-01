package com.example.do_an.ui_user;

public class User_Book {
    private String id;
    private String title;
    private String author;
    private String category;

    // Constructor mặc định (bắt buộc cho Firebase)
    public User_Book() {}

    public User_Book(String id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
