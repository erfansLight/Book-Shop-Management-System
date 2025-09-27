package org.example.bs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setID(rs.getString("bookid"));
                book.setName(rs.getString("bookname"));
                book.setType(rs.getString("type"));
                book.setDescription(rs.getString("description"));
                book.setPrice(rs.getDouble("price"));
                book.setAuthor(rs.getString("author"));
                book.setDate(rs.getDate("date"));

                books.add(book);
            }
        }
        return books;
    }

    public void save(Book book) throws SQLException {
        String sql = "INSERT INTO books (bookid, bookname, type, description, price, author, date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getID());
            stmt.setString(2, book.getName());
            stmt.setString(3, book.getType());
            stmt.setString(4, book.getDescription());
            stmt.setDouble(5, book.getPrice());
            stmt.setString(6, book.getAuthor());
            stmt.setDate(7, new java.sql.Date(book.getDate().getTime()));

            stmt.executeUpdate();
        }
    }

    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET bookid=?, bookname=?, type=?, description=?, price=?, author=?, date=? WHERE id=?";

        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getID());
            stmt.setString(2, book.getName());
            stmt.setString(3, book.getType());
            stmt.setString(4, book.getDescription());
            stmt.setDouble(5, book.getPrice());
            stmt.setString(6, book.getAuthor());
            stmt.setDate(7, new java.sql.Date(book.getDate().getTime()));
            stmt.setInt(8, book.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id=?";

        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean existsByBookId(String bookId) throws SQLException {
        String sql = "SELECT 1 FROM books WHERE bookid = ?";
        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    public int getRealBookId(String bookId) throws SQLException {
        String sql = "SELECT id FROM books WHERE bookid = ?";
        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Book not found for bookid: " + bookId);
                }
            }
        }
    }

    public double getBookPrice(int realBookId) throws SQLException {
        String sql = "SELECT price FROM books WHERE id = ?";
        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, realBookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("price");
                } else {
                    throw new SQLException("Book price not found for id: " + realBookId);
                }
            }
        }
    }
}

