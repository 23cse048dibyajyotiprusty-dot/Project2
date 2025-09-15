package com.ecommerce.dao;

import com.ecommerce.model.Product;
import com.ecommerce.util.DatabaseConnection;
import com.ecommerce.util.SecurityUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product operations
 * Handles all database interactions related to products
 */
public class ProductDAO {
    
    private DatabaseConnection dbConnection;
    
    public ProductDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Get all active products
     * @param limit Maximum number of products to return
     * @param offset Offset for pagination
     * @return List of products
     */
    public List<Product> getAllProducts(int limit, int offset) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.description, p.price, p.stock_quantity, " +
                    "p.category_id, p.image_url, p.is_active, p.created_at, p.updated_at, " +
                    "c.name as category_name " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.category_id " +
                    "WHERE p.is_active = true " +
                    "ORDER BY p.created_at DESC " +
                    "LIMIT ? OFFSET ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all products: " + e.getMessage());
            e.printStackTrace();
        }
        
        return products;
    }
    
    /**
     * Search products with filters
     * @param searchTerm Search term for product name/description
     * @param categoryId Category filter (0 for all categories)
     * @param sortBy Sort order
     * @param minPrice Minimum price filter
     * @param maxPrice Maximum price filter
     * @param page Page number
     * @param pageSize Number of products per page
     * @return List of matching products
     */
    public List<Product> searchProducts(String searchTerm, int categoryId, String sortBy, 
                                      BigDecimal minPrice, BigDecimal maxPrice, int page, int pageSize) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.product_id, p.name, p.description, p.price, p.stock_quantity, ");
        sql.append("p.category_id, p.image_url, p.is_active, p.created_at, p.updated_at, ");
        sql.append("c.name as category_name ");
        sql.append("FROM products p ");
        sql.append("LEFT JOIN categories c ON p.category_id = c.category_id ");
        sql.append("WHERE p.is_active = true ");
        
        List<Object> parameters = new ArrayList<>();
        
        // Add search filter
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.description LIKE ?) ");
            String searchPattern = "%" + SecurityUtil.sanitizeInput(searchTerm.trim()) + "%";
            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }
        
        // Add category filter
        if (categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            parameters.add(categoryId);
        }
        
        // Add price filters
        if (minPrice != null) {
            sql.append("AND p.price >= ? ");
            parameters.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append("AND p.price <= ? ");
            parameters.add(maxPrice);
        }
        
        // Add sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "name_asc":
                    sql.append("ORDER BY p.name ASC ");
                    break;
                case "name_desc":
                    sql.append("ORDER BY p.name DESC ");
                    break;
                case "price_asc":
                    sql.append("ORDER BY p.price ASC ");
                    break;
                case "price_desc":
                    sql.append("ORDER BY p.price DESC ");
                    break;
                case "newest":
                    sql.append("ORDER BY p.created_at DESC ");
                    break;
                default:
                    sql.append("ORDER BY p.created_at DESC ");
            }
        } else {
            sql.append("ORDER BY p.created_at DESC ");
        }
        
        // Add pagination
        int offset = (page - 1) * pageSize;
        sql.append("LIMIT ? OFFSET ?");
        parameters.add(pageSize);
        parameters.add(offset);
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
            e.printStackTrace();
        }
        
        return products;
    }
    
    /**
     * Get product by ID
     * @param productId Product ID
     * @return Product object or null if not found
     */
    public Product getProductById(int productId) {
        String sql = "SELECT p.product_id, p.name, p.description, p.price, p.stock_quantity, " +
                    "p.category_id, p.image_url, p.is_active, p.created_at, p.updated_at, " +
                    "c.name as category_name " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.category_id " +
                    "WHERE p.product_id = ? AND p.is_active = true";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting product by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get products by category
     * @param categoryId Category ID
     * @param limit Maximum number of products
     * @return List of products in the category
     */
    public List<Product> getProductsByCategory(int categoryId, int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.description, p.price, p.stock_quantity, " +
                    "p.category_id, p.image_url, p.is_active, p.created_at, p.updated_at, " +
                    "c.name as category_name " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.category_id " +
                    "WHERE p.category_id = ? AND p.is_active = true " +
                    "ORDER BY p.created_at DESC " +
                    "LIMIT ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            stmt.setInt(2, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting products by category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return products;
    }
    
    /**
     * Get featured products
     * @param limit Maximum number of products
     * @return List of featured products
     */
    public List<Product> getFeaturedProducts(int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.description, p.price, p.stock_quantity, " +
                    "p.category_id, p.image_url, p.is_active, p.created_at, p.updated_at, " +
                    "c.name as category_name " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.category_id " +
                    "WHERE p.is_active = true AND p.stock_quantity > 0 " +
                    "ORDER BY p.created_at DESC " +
                    "LIMIT ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting featured products: " + e.getMessage());
            e.printStackTrace();
        }
        
        return products;
    }
    
    /**
     * Create a new product (Admin only)
     * @param product Product to create
     * @return Generated product ID or -1 if failed
     */
    public int createProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock_quantity, category_id, image_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(product.getName()));
            stmt.setString(2, SecurityUtil.sanitizeInput(product.getDescription()));
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setInt(5, product.getCategoryId());
            stmt.setString(6, SecurityUtil.sanitizeInput(product.getImageUrl()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating product: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Update product information (Admin only)
     * @param product Product with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_quantity = ?, " +
                    "category_id = ?, image_url = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE product_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, SecurityUtil.sanitizeInput(product.getName()));
            stmt.setString(2, SecurityUtil.sanitizeInput(product.getDescription()));
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setInt(5, product.getCategoryId());
            stmt.setString(6, SecurityUtil.sanitizeInput(product.getImageUrl()));
            stmt.setInt(7, product.getProductId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update product stock quantity
     * @param productId Product ID
     * @param newQuantity New stock quantity
     * @return true if successful, false otherwise
     */
    public boolean updateStock(int productId, int newQuantity) {
        String sql = "UPDATE products SET stock_quantity = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE product_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Deactivate product (Admin only)
     * @param productId Product ID
     * @return true if successful, false otherwise
     */
    public boolean deactivateProduct(int productId) {
        String sql = "UPDATE products SET is_active = false, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE product_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deactivating product: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get total product count
     * @return Total number of active products
     */
    public int getTotalProductCount() {
        String sql = "SELECT COUNT(*) FROM products WHERE is_active = true";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting product count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Product object
     * @param rs ResultSet from database query
     * @return Product object
     * @throws SQLException if there's an error reading from ResultSet
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setCategoryName(rs.getString("category_name"));
        product.setImageUrl(rs.getString("image_url"));
        product.setActive(rs.getBoolean("is_active"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return product;
    }
}