package org.example.bs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartRepository {
    private final Connection connection;

    public CartRepository(Connection connection) {
        this.connection = connection;
    }

    public ObservableList<SalesData> getCartItems(int userId) throws SQLException {
        ObservableList<SalesData> list = FXCollections.observableArrayList();
        String sql = "SELECT c.book_id, b.bookid, b.bookname, b.type, b.price, c.quantity, c.added_at " +
                "FROM cart c JOIN books b ON c.book_id = b.id WHERE c.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SalesData sd = new SalesData();
                    sd.setId(rs.getInt("book_id"));
                    sd.setID(rs.getString("bookid"));
                    sd.setName(rs.getString("bookname"));
                    sd.setType(rs.getString("type"));
                    sd.setPrice(rs.getDouble("price"));
                    sd.setQuantity(rs.getInt("quantity"));
                    sd.setDate(rs.getDate("added_at"));
                    list.add(sd);
                }
            }
        }
        return list;
    }

    public boolean isBookInCart(int userId, int bookId) throws SQLException {
        String sql = "SELECT 1 FROM cart WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int getQuantity(int userId, int bookId) throws SQLException {
        String sql = "SELECT quantity FROM cart WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("quantity") : 0;
            }
        }
    }

    public void updateQuantity(int userId, int bookId, int quantity) throws SQLException {
        String sql = "UPDATE cart SET quantity = ?, added_at = CURRENT_TIMESTAMP WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, userId);
            stmt.setInt(3, bookId);
            stmt.executeUpdate();
        }
    }

    public void addToCart(int userId, int bookId, int quantity) throws SQLException {
        String sql = "INSERT INTO cart (user_id, book_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }
    }

    public void removeFromCart(int userId, int bookId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    public void purchaseBook(int userId, int bookId, int quantity, double totalPrice) throws SQLException {
        // Insert into orders
        String insertOrderSql = "INSERT INTO orders (user_id, book_id, quantity, total_price, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = connection.prepareStatement(insertOrderSql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, totalPrice);
            stmt.executeUpdate();
        }

        // Remove from cart
        removeFromCart(userId, bookId);
    }

}
