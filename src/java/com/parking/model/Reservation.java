/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.parking.model;

/**
 *
 * @author Abishek
 */

public class Reservation {
    private int reservationId;
    private String fullName;
    private String email;
    private String vehicleType;
    private String vehiclePlateNumber;
    private Long reservationDate;  
    private Long startTime;       
    private Long endTime;         
    private String spotName;

    public Reservation() {}

    public Reservation(int reservationId, String fullName, String email, String vehicleType, 
                       String vehiclePlateNumber, Long reservationDate, 
                       Long startTime, Long endTime, String spotName) {
        this.reservationId = reservationId;
        this.fullName = fullName;
        this.email = email;
        this.vehicleType = vehicleType;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.spotName = spotName;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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

    public Long getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Long reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }
}
