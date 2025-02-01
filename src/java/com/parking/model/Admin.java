package com.parking.model;

public class Admin {

    private int adminId;
    private String email;
    private String password;
    private String role;

    // Default constructor
    public Admin() {
        this.adminId = 0;
        this.email = "";
        this.password = "";
        this.role = "";
    }

    // Constructor with parameters
    public Admin(int adminId, String email, String password, String role) {
        this.adminId = adminId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
