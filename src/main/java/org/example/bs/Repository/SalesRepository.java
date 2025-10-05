package org.example.bs.Repository;

import org.example.bs.Database;
import org.example.bs.Model.Sales;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class SalesRepository {

    public List<Sales> findAllSales() throws SQLException {
        List<Sales> salesList = new ArrayList<>();

        String sql = """
            SELECT o.id, b.id AS bookID, b.bookname AS bookName, 
                   b.type AS bookType, b.price, o.quantity, o.created_at AS orderDate
            FROM library.orders o
            JOIN library.books b ON o.book_id = b.id
        """;

        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sales sd = new Sales();
                sd.setId(rs.getInt("id"));              // سفارش ID
                sd.setID(rs.getString("bookID"));      // کتاب ID
                sd.setName(rs.getString("bookName"));  // نام کتاب
                sd.setType(rs.getString("bookType"));  // نوع کتاب
                sd.setPrice(rs.getDouble("price"));    // قیمت کتاب
                sd.setQuantity(rs.getInt("quantity")); // تعداد خرید
                sd.setDate(rs.getTimestamp("orderDate"));// تاریخ خرید از created_at
                salesList.add(sd);
            }
        }

        return salesList;
    }

    public double getTotalIncome() throws SQLException {
        String sql = """
            SELECT SUM(b.price * o.quantity) AS total
            FROM library.orders o
            JOIN library.books b ON o.book_id = b.id
        """;

        try (Connection conn = Database.CODB();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }

        return 0.0;
    }
}


