package com.parking.model;

public class Admin {

    private int adminId;
    private String email;
    private String password;
    private String role;

    public Admin() {
        this.adminId = 0;
        this.email = "";
        this.password = "";
        this.role = "";
    }

    public Admin(int adminId, String email, String password, String role) {
        this.adminId = adminId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

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
