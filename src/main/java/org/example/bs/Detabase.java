package org.example.bs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Detabase {
    public static Connection CODB() throws SQLException {
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/test","root","");
        connect.setAutoCommit(true);
        return connect;
    }
}
