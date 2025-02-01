package com.parking.dao;

import com.parking.model.Admin;
import com.parking.util.DbConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {

    private Connection connection;

    public AdminDao() {
        DbConnector dbConnector = new DbConnector();
        this.connection = dbConnector.getConnection();
    }

    public boolean registerAdmin(Admin admin) {
        String query = "INSERT INTO admins (email, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getRole());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }

    public List<Admin> getAllAdmins() {
        String query = "SELECT * FROM admins";
        List<Admin> admins = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                admins.add(extractAdminFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    public Admin getAdminById(int adminId) {
        String query = "SELECT * FROM admins WHERE admin_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, adminId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return extractAdminFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Admin getAdminByEmail(String email) {
        String query = "SELECT * FROM admins WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return extractAdminFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateAdmin(Admin admin) {
        String query = "UPDATE admins SET email = ?, password = ?, role = ? WHERE admin_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getRole());
            stmt.setInt(4, admin.getAdminId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAdmin(int adminId) {
        String query = "DELETE FROM admins WHERE admin_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, adminId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Admin extractAdminFromResultSet(ResultSet resultSet) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminId(resultSet.getInt("admin_id"));
        admin.setEmail(resultSet.getString("email"));
        admin.setPassword(resultSet.getString("password"));
        admin.setRole(resultSet.getString("role"));
        return admin;
    }

    public Admin validateLogin(String email, String password) {
        String query = "SELECT * FROM admins WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);  

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return extractAdminFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
