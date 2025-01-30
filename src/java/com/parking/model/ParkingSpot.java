package com.parking.model;

public class ParkingSpot {
    private int id;
    private String name;
    private String location;
    private String type;
    private double hourlyRate;
    private boolean isAvailable;

    // Constructors
    public ParkingSpot() {}

    public ParkingSpot(int id, String name, String location, String type, double hourlyRate, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.hourlyRate = hourlyRate;
        this.isAvailable = isAvailable;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}