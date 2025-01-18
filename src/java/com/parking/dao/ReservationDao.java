/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parking.dao;

import com.parking.model.Reservation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations for Reservations.
 * Author: Abishek
 */
public class ReservationDao {
    private Connection connection;

    public ReservationDao(Connection connection) {
        this.connection = connection;
    }

    // Add a new reservation to the database
    public boolean addReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (user_id, reservation_date, vehicle_type, vehicle_plate_number, start_time, end_time, parking_spot_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservation.getUserId());
            statement.setDate(2, new java.sql.Date(reservation.getReservationDate().getTime()));
            statement.setString(3, reservation.getVehicleType());
            statement.setString(4, reservation.getVehiclePlateNumber());
            statement.setString(5, reservation.getStartTime());
            statement.setString(6, reservation.getEndTime());
            statement.setInt(7, reservation.getParkingSpotId());
            statement.setString(8, reservation.getStatus());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all reservations
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Reservation reservation = mapResultSetToReservation(resultSet);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Fetch reservations by User ID
    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Reservation reservation = mapResultSetToReservation(resultSet);
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Update a reservation
    public boolean updateReservation(Reservation reservation) {
        String query = "UPDATE reservations SET reservation_date = ?, vehicle_type = ?, vehicle_plate_number = ?, start_time = ?, end_time = ?, parking_spot_id = ?, status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(reservation.getReservationDate().getTime()));
            statement.setString(2, reservation.getVehicleType());
            statement.setString(3, reservation.getVehiclePlateNumber());
            statement.setString(4, reservation.getStartTime());
            statement.setString(5, reservation.getEndTime());
            statement.setInt(6, reservation.getParkingSpotId());
            statement.setString(7, reservation.getStatus());
            statement.setInt(8, reservation.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a reservation by ID
    public boolean deleteReservation(int id) {
        String query = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Utility method to map ResultSet to Reservation object
    private Reservation mapResultSetToReservation(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getInt("id"));
        reservation.setUserId(resultSet.getInt("user_id"));
        reservation.setReservationDate(resultSet.getDate("reservation_date"));
        reservation.setVehicleType(resultSet.getString("vehicle_type"));
        reservation.setVehiclePlateNumber(resultSet.getString("vehicle_plate_number"));
        reservation.setStartTime(resultSet.getString("start_time"));
        reservation.setEndTime(resultSet.getString("end_time"));
        reservation.setParkingSpotId(resultSet.getInt("parking_spot_id"));
        reservation.setStatus(resultSet.getString("status"));
        return reservation;
    }
}
