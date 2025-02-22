package com.example.do_an;

public class HelperClass {
    private String name;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String username;
    private String role;


    public HelperClass(String name, String email, String username, String role) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.role = role;
    }


    public HelperClass() {
        // Default constructor required for Firebase
    }


}

