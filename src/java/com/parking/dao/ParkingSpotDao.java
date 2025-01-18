/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parking.dao;

import com.parking.model.ParkingSpot;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations for Parking Spots.
 * Author: Abishek
 */
public class ParkingSpotDao {
    private Connection connection;

    public ParkingSpotDao(Connection connection) {
        this.connection = connection;
    }

    // Add a new parking spot
    public boolean addParkingSpot(ParkingSpot parkingSpot) {
        String query = "INSERT INTO parking_spots (spot_name, location, spot_type, availability) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, parkingSpot.getSpotName());
            statement.setString(2, parkingSpot.getLocation());
            statement.setString(3, parkingSpot.getSpotType());
            statement.setBoolean(4, parkingSpot.isAvailable());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all parking spots
    public List<ParkingSpot> getAllParkingSpots() {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        String query = "SELECT * FROM parking_spots";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ParkingSpot parkingSpot = mapResultSetToParkingSpot(resultSet);
                parkingSpots.add(parkingSpot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parkingSpots;
    }

    // Fetch a parking spot by ID
    public ParkingSpot getParkingSpotById(int id) {
        String query = "SELECT * FROM parking_spots WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToParkingSpot(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update a parking spot
    public boolean updateParkingSpot(ParkingSpot parkingSpot) {
        String query = "UPDATE parking_spots SET spot_name = ?, location = ?, spot_type = ?, availability = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, parkingSpot.getSpotName());
            statement.setString(2, parkingSpot.getLocation());
            statement.setString(3, parkingSpot.getSpotType());
            statement.setBoolean(4, parkingSpot.isAvailable());
            statement.setInt(5, parkingSpot.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a parking spot by ID
    public boolean deleteParkingSpot(int id) {
        String query = "DELETE FROM parking_spots WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Count all parking spots
    public int countAllParkingSpots() {
        String query = "SELECT COUNT(*) AS total FROM parking_spots";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Count available parking spots
    public int countAvailableParkingSpots() {
        String query = "SELECT COUNT(*) AS total FROM parking_spots WHERE availability = true";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Count booked parking spots
    public int countBookedParkingSpots() {
        String query = "SELECT COUNT(*) AS total FROM parking_spots WHERE availability = false";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Utility method to map ResultSet to ParkingSpot object
    private ParkingSpot mapResultSetToParkingSpot(ResultSet resultSet) throws SQLException {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setId(resultSet.getInt("id"));
        parkingSpot.setSpotName(resultSet.getString("spot_name"));
        parkingSpot.setLocation(resultSet.getString("location"));
        parkingSpot.setSpotType(resultSet.getString("spot_type"));
        parkingSpot.setAvailable(resultSet.getBoolean("availability"));
        return parkingSpot;
    }
}
