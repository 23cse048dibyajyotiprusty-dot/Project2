package com.ecommerce.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Order Model Class
 * Represents a customer order in the e-commerce application
 */
public class Order {
    private int orderId;
    private int userId;
    private String userName;
    private BigDecimal totalAmount;
    private String status;
    private int shippingAddressId;
    private String shippingAddress;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<OrderItem> orderItems;
    private Payment payment;
    
    // Order status constants
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_PROCESSING = "processing";
    public static final String STATUS_SHIPPED = "shipped";
    public static final String STATUS_DELIVERED = "delivered";
    public static final String STATUS_CANCELLED = "cancelled";
    
    // Default constructor
    public Order() {
    }
    
    // Constructor with essential fields
    public Order(int userId, BigDecimal totalAmount, int shippingAddressId) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.shippingAddressId = shippingAddressId;
        this.status = STATUS_PENDING;
    }
    
    // Constructor with all main fields
    public Order(int orderId, int userId, BigDecimal totalAmount, String status, 
                int shippingAddressId, Timestamp createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddressId = shippingAddressId;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getShippingAddressId() {
        return shippingAddressId;
    }
    
    public void setShippingAddressId(int shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
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
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    public Payment getPayment() {
        return payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    // Utility methods
    
    /**
     * Get formatted total amount
     * @return Formatted total amount as string
     */
    public String getFormattedTotalAmount() {
        return String.format("₹%.2f", totalAmount);
    }
    
    /**
     * Get formatted order date
     * @return Formatted creation date
     */
    public String getFormattedOrderDate() {
        if (createdAt == null) {
            return "";
        }
        return createdAt.toString().substring(0, 19); // Remove milliseconds
    }
    
    /**
     * Check if order can be cancelled
     * @return true if order can be cancelled
     */
    public boolean canBeCancelled() {
        return STATUS_PENDING.equals(status) || STATUS_CONFIRMED.equals(status);
    }
    
    /**
     * Check if order is completed
     * @return true if order is delivered
     */
    public boolean isCompleted() {
        return STATUS_DELIVERED.equals(status);
    }
    
    /**
     * Check if order is cancelled
     * @return true if order is cancelled
     */
    public boolean isCancelled() {
        return STATUS_CANCELLED.equals(status);
    }
    
    /**
     * Get status display text with proper formatting
     * @return Formatted status text
     */
    public String getStatusDisplay() {
        if (status == null) {
            return "Unknown";
        }
        
        return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
    }
    
    /**
     * Get status color class for UI
     * @return CSS class name for status color
     */
    public String getStatusColorClass() {
        switch (status) {
            case STATUS_PENDING:
                return "text-warning";
            case STATUS_CONFIRMED:
            case STATUS_PROCESSING:
                return "text-info";
            case STATUS_SHIPPED:
                return "text-primary";
            case STATUS_DELIVERED:
                return "text-success";
            case STATUS_CANCELLED:
                return "text-danger";
            default:
                return "text-secondary";
        }
    }
    
    /**
     * Calculate total number of items in the order
     * @return Total quantity of all items
     */
    public int getTotalItemCount() {
        if (orderItems == null) {
            return 0;
        }
        
        return orderItems.stream()
                       .mapToInt(OrderItem::getQuantity)
                       .sum();
    }
    
    /**
     * Get estimated delivery date (7 days from order date)
     * @return Estimated delivery timestamp
     */
    public Timestamp getEstimatedDeliveryDate() {
        if (createdAt == null) {
            return null;
        }
        
        long deliveryTime = createdAt.getTime() + (7 * 24 * 60 * 60 * 1000L); // 7 days
        return new Timestamp(deliveryTime);
    }
    
    /**
     * Check if order is recent (within last 30 days)
     * @return true if order is recent
     */
    public boolean isRecent() {
        if (createdAt == null) {
            return false;
        }
        
        long thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L);
        return createdAt.getTime() > thirtyDaysAgo;
    }
    
    @Override
    public String toString() {
        return "Order{" +
               "orderId=" + orderId +
               ", userId=" + userId +
               ", userName='" + userName + '\'' +
               ", totalAmount=" + totalAmount +
               ", status='" + status + '\'' +
               ", createdAt=" + createdAt +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Order order = (Order) obj;
        return orderId == order.orderId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(orderId);
    }
}