package org.example.bs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection CODB() throws SQLException {
        String url = "jdbc:postgresql://78.38.35.219:5432/402463139?currentSchema=library";
        String user = "402463139";
        String password = "402463139";
        Connection connect = DriverManager.getConnection(url, user, password);
        connect.setAutoCommit(true);
        System.out.println("Connected to database successfully");
        return connect;
    }
}
//        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/test","root","");
