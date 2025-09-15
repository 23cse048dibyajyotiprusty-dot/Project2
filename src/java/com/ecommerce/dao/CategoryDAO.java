package com.ecommerce.dao;

import com.ecommerce.model.Category;
import com.ecommerce.util.DatabaseConnection;
import com.ecommerce.util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Category operations
 * Handles all database interactions related to categories
 */
public class CategoryDAO {
    
    private DatabaseConnection dbConnection;
    
    public CategoryDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Get all active categories
     * @return List of active categories
     */
    public List<Category> getAllActiveCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT c.category_id, c.name, c.description, c.image_url, c.is_active, c.created_at, " +
                    "COUNT(p.product_id) as product_count " +
                    "FROM categories c " +
                    "LEFT JOIN products p ON c.category_id = p.category_id AND p.is_active = true " +
                    "WHERE c.is_active = true " +
                    "GROUP BY c.category_id, c.name, c.description, c.image_url, c.is_active, c.created_at " +
                    "ORDER BY c.name ASC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all active categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return categories;
    }
    
    /**
     * Get category by ID
     * @param categoryId Category ID
     * @return Category object or null if not found
     */
    public Category getCategoryById(int categoryId) {
        String sql = "SELECT c.category_id, c.name, c.description, c.image_url, c.is_active, c.created_at, " +
                    "COUNT(p.product_id) as product_count " +
                    "FROM categories c " +
                    "LEFT JOIN products p ON c.category_id = p.category_id AND p.is_active = true " +
                    "WHERE c.category_id = ? " +
                    "GROUP BY c.category_id, c.name, c.description, c.image_url, c.is_active, c.created_at";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting category by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all categories (including inactive ones) - Admin only
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT c.category_id, c.name, c.description, c.image_url, c.is_active, c.created_at, " +
                    "COUNT(p.product_id) as product_count " +
                    "FROM categories c " +
                    "LEFT JOIN products p ON c.category_id = p.category_id AND p.is_active = true " +
                    "GROUP BY c.category_id, c.name, c.description, c.image_url, c.is_active, c.created_at " +
                    "ORDER BY c.name ASC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return categories;
    }
    
    /**
     * Create a new category (Admin only)
     * @param category Category to create
     * @return Generated category ID or -1 if failed
     */
    public int createCategory(Category category) {
        String sql = "INSERT INTO categories (name, description, image_url) VALUES (?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(category.getName()));
            stmt.setString(2, SecurityUtil.sanitizeInput(category.getDescription()));
            stmt.setString(3, SecurityUtil.sanitizeInput(category.getImageUrl()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Update category information (Admin only)
     * @param category Category with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ?, image_url = ? WHERE category_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(category.getName()));
            stmt.setString(2, SecurityUtil.sanitizeInput(category.getDescription()));
            stmt.setString(3, SecurityUtil.sanitizeInput(category.getImageUrl()));
            stmt.setInt(4, category.getCategoryId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Deactivate category (Admin only)
     * @param categoryId Category ID
     * @return true if successful, false otherwise
     */
    public boolean deactivateCategory(int categoryId) {
        String sql = "UPDATE categories SET is_active = false WHERE category_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deactivating category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Activate category (Admin only)
     * @param categoryId Category ID
     * @return true if successful, false otherwise
     */
    public boolean activateCategory(int categoryId) {
        String sql = "UPDATE categories SET is_active = true WHERE category_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error activating category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if category name already exists
     * @param name Category name to check
     * @param excludeId Category ID to exclude from check (for updates)
     * @return true if name exists, false otherwise
     */
    public boolean categoryNameExists(String name, int excludeId) {
        String sql = "SELECT COUNT(*) FROM categories WHERE name = ? AND category_id != ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(name));
            stmt.setInt(2, excludeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking category name existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get total category count
     * @return Total number of categories
     */
    public int getTotalCategoryCount() {
        String sql = "SELECT COUNT(*) FROM categories";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting category count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get active category count
     * @return Number of active categories
     */
    public int getActiveCategoryCount() {
        String sql = "SELECT COUNT(*) FROM categories WHERE is_active = true";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active category count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Category object
     * @param rs ResultSet from database query
     * @return Category object
     * @throws SQLException if there's an error reading from ResultSet
     */
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setImageUrl(rs.getString("image_url"));
        category.setActive(rs.getBoolean("is_active"));
        category.setCreatedAt(rs.getTimestamp("created_at"));
        category.setProductCount(rs.getInt("product_count"));
        
        return category;
    }
}