package com.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String dbUrl;
    private static HikariDataSource dataSource;

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

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(5);
        config.setIdleTimeout(60000);
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(1800000);

        config.setAutoCommit(true);
        config.setConnectionTestQuery("SELECT 1");

        config.setLeakDetectionThreshold(60000);

        config.setPoolName("Exchanger");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Database connection pool not initialized. Call init() method first.");
        }
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
