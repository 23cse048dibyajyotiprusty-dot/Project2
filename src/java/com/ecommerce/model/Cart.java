package com.ecommerce.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Cart Model Class
 * Represents shopping cart items
 */
public class Cart {
    private int cartId;
    private int userId;
    private int productId;
    private String productName;
    private String productImage;
    private BigDecimal productPrice;
    private int quantity;
    private BigDecimal subtotal;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int stockQuantity;
    
    public Cart() {}
    
    public Cart(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    
    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { 
        this.productPrice = productPrice;
        updateSubtotal();
    }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        updateSubtotal();
    }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    
    private void updateSubtotal() {
        if (productPrice != null && quantity > 0) {
            this.subtotal = productPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    public String getFormattedPrice() {
        return String.format("₹%.2f", productPrice);
    }
    
    public String getFormattedSubtotal() {
        return String.format("₹%.2f", subtotal);
    }
    
    public boolean isAvailable() {
        return quantity <= stockQuantity;
    }
}