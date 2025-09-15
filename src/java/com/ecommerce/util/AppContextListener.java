package com.ecommerce.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Application Context Listener
 * Handles application startup and shutdown tasks
 */
public class AppContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== ShopEasy E-Commerce Application Starting ===");
        
        // Initialize database connection pool
        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            if (dbConnection.testConnection()) {
                System.out.println("✓ Database connection established successfully");
                System.out.println("✓ " + dbConnection.getConnectionPoolStatus());
            } else {
                System.err.println("✗ Database connection failed!");
            }
        } catch (Exception e) {
            System.err.println("✗ Error initializing database: " + e.getMessage());
        }
        
        // Set application context attributes
        String appName = sce.getServletContext().getInitParameter("application.name");
        String appVersion = sce.getServletContext().getInitParameter("application.version");
        
        if (appName != null) {
            sce.getServletContext().setAttribute("appName", appName);
        }
        if (appVersion != null) {
            sce.getServletContext().setAttribute("appVersion", appVersion);
        }
        
        System.out.println("✓ Application: " + (appName != null ? appName : "ShopEasy") + 
                          " v" + (appVersion != null ? appVersion : "1.0.0"));
        System.out.println("✓ Security filters initialized");
        System.out.println("✓ Character encoding set to UTF-8");
        System.out.println("=== Application Started Successfully ===");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== ShopEasy E-Commerce Application Shutting Down ===");
        
        // Shutdown database connection pool
        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            dbConnection.shutdown();
            System.out.println("✓ Database connections closed");
        } catch (Exception e) {
            System.err.println("✗ Error shutting down database connections: " + e.getMessage());
        }
        
        System.out.println("=== Application Shutdown Complete ===");
    }
}