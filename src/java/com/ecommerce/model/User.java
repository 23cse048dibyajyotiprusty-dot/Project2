package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * User Model Class
 * Represents a customer user in the e-commerce application
 */
public class User {
    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String phone;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isActive;
    
    // Default constructor
    public User() {
    }
    
    // Constructor with essential fields
    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isActive = true;
    }
    
    // Constructor with all fields except timestamps
    public User(int userId, String name, String email, String passwordHash, String phone, boolean isActive) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // Utility methods
    
    /**
     * Get display name (first name)
     * @return First part of the full name
     */
    public String getDisplayName() {
        if (name != null && name.contains(" ")) {
            return name.split(" ")[0];
        }
        return name;
    }
    
    /**
     * Check if user has complete profile information
     * @return true if all essential fields are filled
     */
    public boolean hasCompleteProfile() {
        return name != null && !name.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               phone != null && !phone.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "User{" +
               "userId=" + userId +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return userId == user.userId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }
}