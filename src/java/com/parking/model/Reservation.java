package com.parking.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Reservation {

    private int reservationId;
    private int userId;
    private String fullName;
    private String email;
    private int parkingSpotId;
    private String vehicleType;
    private String vehiclePlateNumber;
    private Date reservationDate;
    private Timestamp startTime;
    private Timestamp endTime;

    // Constructor
    public Reservation() {}

    public Reservation(int userId, String fullName, String email, int parkingSpotId, 
                       String vehicleType, String vehiclePlateNumber, 
                       Date reservationDate, Timestamp startTime, Timestamp endTime) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.parkingSpotId = parkingSpotId;
        this.vehicleType = vehicleType;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
