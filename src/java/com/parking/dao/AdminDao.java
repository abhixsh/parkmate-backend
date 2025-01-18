package com.parking.dao;

import com.parking.model.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
    private Connection connection;

    public AdminDao(Connection connection) {
        this.connection = connection;
    }

    // Add a new admin to the database
    public boolean addAdmin(Admin admin) {
        String query = "INSERT INTO admins (full_name, email, phone_number, username, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, admin.getFullName());
            statement.setString(2, admin.getEmail());
            statement.setString(3, admin.getPhoneNumber());
            statement.setString(4, admin.getUsername());
            statement.setString(5, admin.getPassword());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate admin login
    public Admin validateLogin(String username, String password) {
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Admin admin = new Admin();
                    admin.setId(resultSet.getInt("id"));
                    admin.setFullName(resultSet.getString("full_name"));
                    admin.setEmail(resultSet.getString("email"));
                    admin.setPhoneNumber(resultSet.getString("phone_number"));
                    admin.setUsername(resultSet.getString("username"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve admin details by ID
    public Admin getAdminById(int id) {
        String query = "SELECT * FROM admins WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Admin admin = new Admin();
                    admin.setId(resultSet.getInt("id"));
                    admin.setFullName(resultSet.getString("full_name"));
                    admin.setEmail(resultSet.getString("email"));
                    admin.setPhoneNumber(resultSet.getString("phone_number"));
                    admin.setUsername(resultSet.getString("username"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update admin information
    public boolean updateAdmin(Admin admin) {
        String query = "UPDATE admins SET full_name = ?, email = ?, phone_number = ?, username = ?, password = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, admin.getFullName());
            statement.setString(2, admin.getEmail());
            statement.setString(3, admin.getPhoneNumber());
            statement.setString(4, admin.getUsername());
            statement.setString(5, admin.getPassword());
            statement.setInt(6, admin.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete admin by ID
    public boolean deleteAdmin(int id) {
        String query = "DELETE FROM admins WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
