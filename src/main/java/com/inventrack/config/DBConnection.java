package com.inventrack.config;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Provides JDBC connections to the application's MySQL database.
 *
 * <p>This project uses direct JDBC access (no connection pool). A new connection is opened per call and
 * typically managed via try-with-resources by DAO classes.</p>
 *
 * <p>Note: Credentials are currently hard-coded for local development. In production, move these values
 * to environment variables or an external configuration file.</p>
 */
public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventrack_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@1234";

    /**
     * Opens a new JDBC connection using the configured URL/user/password.
     *
     * @return a live {@link Connection} to the database
     * @throws Exception if the MySQL driver cannot be loaded or the connection fails
     */
    public static Connection getConnection() throws Exception {
        // Step 1: Ensure the MySQL JDBC driver is available/loaded.
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Step 2: Open a new connection (callers should close it via try-with-resources).
        return DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
    }
}
