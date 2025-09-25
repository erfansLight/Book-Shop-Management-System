package org.example.bs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistRepository {

    private final Connection connect;

    public WishlistRepository(Connection connect) {
        this.connect = connect;
    }

    public boolean isBookInWishlist(int userId, int bookId) throws SQLException {
        String sql = "SELECT 1 FROM wishlist WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void addBookToWishlist(int userId, int bookId) throws SQLException {
        String sql = "INSERT INTO wishlist (user_id, book_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    public void removeBookFromWishlist(int userId, int wishlistId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE id = ? AND user_id = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public List<wishData> getWishlist(int userId) throws SQLException {
        List<wishData> list = new ArrayList<>();
        String sql = "SELECT w.id, b.bookid, b.bookname, b.type, b.description, b.price, b.author, b.date " +
                "FROM wishlist w JOIN books b ON w.book_id = b.id " +
                "WHERE w.user_id = ?";

        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new wishData(
                            rs.getInt("id"),
                            rs.getString("bookid"),
                            rs.getString("bookname"),
                            rs.getString("type"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("author"),
                            rs.getDate("date")
                    ));
                }
            }
        }
        return list;
    }
}
