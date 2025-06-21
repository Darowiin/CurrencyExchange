package com.utils;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String dbUrl;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    public static void init(ServletContext context) {
        String dbPath = context.getRealPath("/WEB-INF/Exchanger.db");
        dbUrl = "jdbc:sqlite:" + dbPath;
    }

    public static Connection getConnection() throws SQLException {
        if (dbUrl == null) {
            throw new SQLException("Database URL not initialized. Call init() method first.");
        }
        return DriverManager.getConnection(dbUrl);
    }
}

