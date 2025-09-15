package com.ecommerce.model;

import java.math.BigDecimal;

/**
 * OrderItem Model Class
 * Represents individual items within an order
 */
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int productId;
    private String productName;
    private String productImage;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    
    public OrderItem() {}
    
    public OrderItem(int orderId, int productId, int quantity, BigDecimal price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }
    
    // Getters and Setters
    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }
    
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        updateSubtotal();
    }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { 
        this.price = price;
        updateSubtotal();
    }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    private void updateSubtotal() {
        if (price != null && quantity > 0) {
            this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    public String getFormattedPrice() {
        return String.format("₹%.2f", price);
    }
    
    public String getFormattedSubtotal() {
        return String.format("₹%.2f", subtotal);
    }
}