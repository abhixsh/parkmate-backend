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

    public boolean addReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (fullName, email, vehicleType, vehiclePlateNumber, reservationDate, startTime, endTime, spotName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, reservation.getFullName());
            stmt.setString(2, reservation.getEmail());
            stmt.setString(3, reservation.getVehicleType());
            stmt.setString(4, reservation.getVehiclePlateNumber());
            stmt.setLong(5, reservation.getReservationDate());  
            stmt.setLong(6, reservation.getStartTime());        
            stmt.setLong(7, reservation.getEndTime());          
            stmt.setString(8, reservation.getSpotName());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Reservation getReservationById(int id) {
        String query = "SELECT * FROM reservations WHERE reservationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return new Reservation(
                    resultSet.getInt("reservationID"),
                    resultSet.getString("fullName"),
                    resultSet.getString("email"),
                    resultSet.getString("vehicleType"),
                    resultSet.getString("vehiclePlateNumber"),
                    resultSet.getLong("reservationDate"),  
                    resultSet.getLong("startTime"),        
                    resultSet.getLong("endTime"),         
                    resultSet.getString("spotName")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                reservations.add(new Reservation(
                    resultSet.getInt("reservationID"),
                    resultSet.getString("fullName"),
                    resultSet.getString("email"),
                    resultSet.getString("vehicleType"),
                    resultSet.getString("vehiclePlateNumber"),
                    resultSet.getLong("reservationDate"),  
                    resultSet.getLong("startTime"),        
                    resultSet.getLong("endTime"),         
                    resultSet.getString("spotName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // UPDATE: Update an existing reservation
    public boolean updateReservation(Reservation reservation) {
        String query = "UPDATE reservations SET fullName = ?, email = ?, vehicleType = ?, vehiclePlateNumber = ?, startTime = ?, endTime = ?, spotName = ? WHERE reservationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, reservation.getFullName());
            stmt.setString(2, reservation.getEmail());
            stmt.setString(3, reservation.getVehicleType());
            stmt.setString(4, reservation.getVehiclePlateNumber());
            stmt.setLong(5, reservation.getStartTime());  
            stmt.setLong(6, reservation.getEndTime());    
            stmt.setString(7, reservation.getSpotName());
            stmt.setInt(8, reservation.getReservationId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteReservation(int id) {
        String query = "DELETE FROM reservations WHERE reservationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
public List<Reservation> getReservationsByEmail(String email) {
    List<Reservation> reservations = new ArrayList<>();
    String query = "SELECT * FROM reservations WHERE email = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, email);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            reservations.add(new Reservation(
                resultSet.getInt("reservationID"),
                resultSet.getString("fullName"),
                resultSet.getString("email"),
                resultSet.getString("vehicleType"),
                resultSet.getString("vehiclePlateNumber"),
                resultSet.getLong("reservationDate"),
                resultSet.getLong("startTime"),
                resultSet.getLong("endTime"),
                resultSet.getString("spotName")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return reservations;
}

}
