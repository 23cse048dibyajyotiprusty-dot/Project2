package com.ecommerce.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Database Connection Utility Class
 * Provides database connections with simple connection pooling
 * Implements singleton pattern for efficient resource management
 */
public class DatabaseConnection {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Prusty@123"; // Change this to your MySQL password
    
    private static final int MAX_POOL_SIZE = 10;
    private static final int MIN_POOL_SIZE = 3;
    
    private static DatabaseConnection instance;
    private BlockingQueue<Connection> connectionPool;
    private AtomicInteger connectionCount;
    
    // Private constructor for singleton pattern
    private DatabaseConnection() {
        connectionPool = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
        connectionCount = new AtomicInteger(0);
        initializeConnectionPool();
    }
    
    /**
     * Get singleton instance of DatabaseConnection
     * @return DatabaseConnection instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Initialize the connection pool with minimum connections
     */
    private void initializeConnectionPool() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create initial connections
            for (int i = 0; i < MIN_POOL_SIZE; i++) {
                Connection conn = createNewConnection();
                if (conn != null) {
                    connectionPool.offer(conn);
                    connectionCount.incrementAndGet();
                }
            }
            
            System.out.println("Connection pool initialized with " + connectionCount.get() + " connections");
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }
    
    /**
     * Create a new database connection
     * @return New Connection object
     */
    private Connection createNewConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error creating new connection: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get database connection from connection pool
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        try {
            // Try to get connection from pool first
            Connection conn = connectionPool.poll();
            
            // If no connection available in pool, create new one if under limit
            if (conn == null) {
                if (connectionCount.get() < MAX_POOL_SIZE) {
                    conn = createNewConnection();
                    if (conn != null) {
                        connectionCount.incrementAndGet();
                    }
                } else {
                    // Wait for available connection
                    conn = connectionPool.take();
                }
            }
            
            // Validate connection
            if (conn != null && (conn.isClosed() || !conn.isValid(2))) {
                conn.close();
                connectionCount.decrementAndGet();
                conn = createNewConnection();
                if (conn != null) {
                    connectionCount.incrementAndGet();
                }
            }
            
            if (conn == null) {
                throw new SQLException("Unable to obtain database connection");
            }
            
            return conn;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while waiting for connection", e);
        }
    }
    
    /**
     * Return connection to pool
     * @param connection Connection to be returned to pool
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed() && connection.isValid(2)) {
                    // Return connection to pool if it's still valid
                    if (!connectionPool.offer(connection)) {
                        // Pool is full, close the connection
                        connection.close();
                        connectionCount.decrementAndGet();
                    }
                } else {
                    // Connection is invalid, close it
                    connection.close();
                    connectionCount.decrementAndGet();
                }
            } catch (SQLException e) {
                System.err.println("Error returning connection to pool: " + e.getMessage());
                try {
                    connection.close();
                    connectionCount.decrementAndGet();
                } catch (SQLException ex) {
                    System.err.println("Error closing invalid connection: " + ex.getMessage());
                }
            }
        }
    }
    
    /**
     * Test database connectivity
     * @return true if connection is successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get current connection pool status (for debugging)
     * @return String containing pool status information
     */
    public String getConnectionPoolStatus() {
        return String.format(
            "Pool Status - Available: %d, Total Created: %d, Max Pool Size: %d",
            connectionPool.size(),
            connectionCount.get(),
            MAX_POOL_SIZE
        );
    }
    
    /**
     * Shutdown the connection pool (cleanup)
     */
    public void shutdown() {
        System.out.println("Shutting down connection pool...");
        Connection conn;
        while ((conn = connectionPool.poll()) != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection during shutdown: " + e.getMessage());
            }
        }
        connectionCount.set(0);
        System.out.println("Connection pool shutdown complete.");
    }
}
