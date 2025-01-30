package com.parking.dao;

import com.parking.model.Reservation;
import com.parking.util.DbConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {

    private Connection connection;

    public ReservationDao() {
        DbConnector dbConnector = new DbConnector();
        this.connection = dbConnector.getConnection();
    }

    // CREATE: Add a new reservation
    public boolean addReservation(Reservation reservation) {
        String query = "INSERT INTO Reservations (userID, parkingSpotID, fullName, email, vehicleType, vehiclePlateNumber, reservationDate, startTime, endTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getParkingSpotId());
            stmt.setString(3, reservation.getFullName());
            stmt.setString(4, reservation.getEmail());
            stmt.setString(5, reservation.getVehicleType());
            stmt.setString(6, reservation.getVehiclePlateNumber());
            stmt.setDate(7, reservation.getReservationDate());
            stmt.setTimestamp(8, reservation.getStartTime());
            stmt.setTimestamp(9, reservation.getEndTime());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ: Get a reservation by ID
    public Reservation getReservationById(int id) {
        String query = "SELECT * FROM Reservations WHERE reservationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return new Reservation(
                    resultSet.getInt("userID"),
                    resultSet.getString("fullName"),
                    resultSet.getString("email"),
                    resultSet.getInt("parkingSpotID"),
                    resultSet.getString("vehicleType"),
                    resultSet.getString("vehiclePlateNumber"),
                    resultSet.getDate("reservationDate"),
                    resultSet.getTimestamp("startTime"),
                    resultSet.getTimestamp("endTime")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ: Get all reservations
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservations";
        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                reservations.add(new Reservation(
                    resultSet.getInt("userID"),
                    resultSet.getString("fullName"),
                    resultSet.getString("email"),
                    resultSet.getInt("parkingSpotID"),
                    resultSet.getString("vehicleType"),
                    resultSet.getString("vehiclePlateNumber"),
                    resultSet.getDate("reservationDate"),
                    resultSet.getTimestamp("startTime"),
                    resultSet.getTimestamp("endTime")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // UPDATE: Update a reservation
    public boolean updateReservation(Reservation reservation) {
        String query = "UPDATE Reservations SET fullName = ?, email = ?, vehicleType = ?, vehiclePlateNumber = ?, startTime = ?, endTime = ? WHERE reservationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, reservation.getFullName());
            stmt.setString(2, reservation.getEmail());
            stmt.setString(3, reservation.getVehicleType());
            stmt.setString(4, reservation.getVehiclePlateNumber());
            stmt.setTimestamp(5, reservation.getStartTime());
            stmt.setTimestamp(6, reservation.getEndTime());
            stmt.setInt(7, reservation.getReservationId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE: Delete a reservation by ID
    public boolean deleteReservation(int id) {
        String query = "DELETE FROM Reservations WHERE reservationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
