package org.example.bs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AuthRepository {

    // پیدا کردن کاربر با نام و پسورد (Login)
    public User findByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT id, name, role FROM users WHERE name = ? AND password = ?";
        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        }
        return null;
    }

    // ثبت‌نام کاربر جدید
    public boolean createUser(User user) throws SQLException {
        String insertUser = "INSERT INTO users (name, password, city, date, email, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(insertUser)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getCity());
            stmt.setDate(4, java.sql.Date.valueOf(user.getDate()));
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getRole());
            return stmt.executeUpdate() > 0;
        }
    }

    // تغییر رمز عبور
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        String updateQuery = "UPDATE users SET password = ? WHERE name = ?";
        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        }
    }

    // پیدا کردن کاربر با جزئیات (برای بازیابی رمز)
    public User findUserByDetails(String username, String city, LocalDate birthday) throws SQLException {
        String query = "SELECT id, name, role FROM users WHERE name = ? AND city = ? AND date = ?";
        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, city);
            stmt.setDate(3, java.sql.Date.valueOf(birthday));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        }
        return null;
    }

    // بررسی وجود نام کاربری یا ایمیل (برای ثبت‌نام)
    public boolean existsByUsernameOrEmail(String username, String email) throws SQLException {
        String query = "SELECT 1 FROM users WHERE name = ? OR email = ?";
        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
