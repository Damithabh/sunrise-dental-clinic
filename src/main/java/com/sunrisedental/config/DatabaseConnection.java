package com.sunrisedental.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton design pattern for establishing a secure Database Connection.
 * 
 * Ensures that only one instance of the database connection manager is created,
 * reducing the overhead of repeatedly opening and closing connections.
 * 
 * Design Pattern: Singleton
 */
@Component
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private DatabaseConnection() {
        // Initialization can happen lazily or via Spring.
    }

    /**
     * Global access point to get the single instance of the class.
     * 
     * @return DatabaseConnection singleton instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Retrieves the active database connection, establishing it if necessary.
     * 
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Hardcoding fallback credentials for the raw JDBC demonstration
                String connectUrl = (url != null) ? url : "jdbc:mysql://localhost:3306/sunrise_dental_db?useSSL=false&serverTimezone=UTC";
                String connectUser = (username != null) ? username : "dental_admin";
                String connectPass = (password != null) ? password : "your_password_here";

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(connectUrl, connectUser, connectPass);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found.", e);
            }
        }
        return connection;
    }
}
