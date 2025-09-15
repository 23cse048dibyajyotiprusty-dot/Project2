package com.ecommerce.dao;

import com.ecommerce.model.User;
import com.ecommerce.util.DatabaseConnection;
import com.ecommerce.util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User operations
 * Handles all database interactions related to users
 */
public class UserDAO {
    
    private DatabaseConnection dbConnection;
    
    public UserDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Create a new user account
     * @param user User object with account details
     * @return Generated user ID, or -1 if failed
     */
    public int createUser(User user) {
        String sql = "INSERT INTO users (name, email, password_hash, phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(user.getName()));
            stmt.setString(2, SecurityUtil.sanitizeInput(user.getEmail().toLowerCase()));
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, SecurityUtil.sanitizeInput(user.getPhone()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        user.setUserId(userId);
                        return userId;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Authenticate user login
     * @param email User email
     * @param password Plain text password
     * @return User object if authenticated, null otherwise
     */
    public User authenticateUser(String email, String password) {
        String sql = "SELECT user_id, name, email, password_hash, phone, is_active, created_at, updated_at " +
                    "FROM users WHERE email = ? AND is_active = true";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(email.toLowerCase()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    
                    if (SecurityUtil.verifyPassword(password, storedHash)) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setName(rs.getString("name"));
                        user.setEmail(rs.getString("email"));
                        user.setPasswordHash(storedHash);
                        user.setPhone(rs.getString("phone"));
                        user.setActive(rs.getBoolean("is_active"));
                        user.setCreatedAt(rs.getTimestamp("created_at"));
                        user.setUpdatedAt(rs.getTimestamp("updated_at"));
                        
                        return user;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get user by ID
     * @param userId User ID
     * @return User object or null if not found
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, name, email, password_hash, phone, is_active, created_at, updated_at " +
                    "FROM users WHERE user_id = ? AND is_active = true";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setPhone(rs.getString("phone"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    
                    return user;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get user by email
     * @param email User email
     * @return User object or null if not found
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, name, email, password_hash, phone, is_active, created_at, updated_at " +
                    "FROM users WHERE email = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(email.toLowerCase()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setPhone(rs.getString("phone"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    
                    return user;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Update user profile
     * @param user User object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, phone = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(user.getName()));
            stmt.setString(2, SecurityUtil.sanitizeInput(user.getPhone()));
            stmt.setInt(3, user.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update user password
     * @param userId User ID
     * @param newPassword New plain text password
     * @return true if successful, false otherwise
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String hashedPassword = SecurityUtil.hashPassword(newPassword);
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if email already exists
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(email.toLowerCase()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Deactivate user account
     * @param userId User ID
     * @return true if successful, false otherwise
     */
    public boolean deactivateUser(int userId) {
        String sql = "UPDATE users SET is_active = false, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deactivating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get all users (for admin purposes)
     * @param limit Number of users to fetch
     * @param offset Offset for pagination
     * @return List of users
     */
    public List<User> getAllUsers(int limit, int offset) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, phone, is_active, created_at, updated_at " +
                    "FROM users ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    
                    users.add(user);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return users;
    }
    
    /**
     * Get total user count
     * @return Total number of users
     */
    public int getTotalUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Search users by name or email
     * @param searchTerm Search term
     * @param limit Number of results to return
     * @param offset Offset for pagination
     * @return List of matching users
     */
    public List<User> searchUsers(String searchTerm, int limit, int offset) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, phone, is_active, created_at, updated_at " +
                    "FROM users WHERE (name LIKE ? OR email LIKE ?) " +
                    "ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + SecurityUtil.sanitizeInput(searchTerm) + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    
                    users.add(user);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return users;
    }
}