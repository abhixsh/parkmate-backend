package com.parking.dao;

import com.parking.model.ParkingSpot;
import com.parking.util.DbConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotDao {
    private Connection connection;

    public ParkingSpotDao() {
        DbConnector dbConnector = new DbConnector();
        this.connection = dbConnector.getConnection();
    }

    // Add a new parking spot
    public boolean addParkingSpot(ParkingSpot spot) {
        String query = "INSERT INTO ParkingSpots (name, location, type, hourlyRate, isAvailable) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, spot.getName());
            stmt.setString(2, spot.getLocation());
            stmt.setString(3, spot.getType());
            stmt.setDouble(4, spot.getHourlyRate());
            stmt.setBoolean(5, spot.isAvailable());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all parking spots
    public List<ParkingSpot> getAllParkingSpots() {
        List<ParkingSpot> spots = new ArrayList<>();
        String query = "SELECT * FROM ParkingSpots";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                spots.add(new ParkingSpot(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("location"),
                    resultSet.getString("type"),
                    resultSet.getDouble("hourlyRate"),
                    resultSet.getBoolean("isAvailable")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }
    public ParkingSpot getParkingSpotById(int id) {
        String query = "SELECT * FROM ParkingSpots WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return new ParkingSpot(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("location"),
                    resultSet.getString("type"),
                    resultSet.getDouble("hourlyRate"),
                    resultSet.getBoolean("isAvailable")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateParkingSpot(ParkingSpot spot) {
        String query = "UPDATE ParkingSpots SET name = ?, location = ?, type = ?, hourlyRate = ?, isAvailable = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, spot.getName());
            stmt.setString(2, spot.getLocation());
            stmt.setString(3, spot.getType());
            stmt.setDouble(4, spot.getHourlyRate());
            stmt.setBoolean(5, spot.isAvailable());
            stmt.setInt(6, spot.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteParkingSpot(int id) {
        String query = "DELETE FROM ParkingSpots WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}