package com.example.do_an.ui_user;

public class User_Category {
    private String id;
    private String name;

    public User_Category() {
        // Firebase cần constructor rỗng
    }

    public User_Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}