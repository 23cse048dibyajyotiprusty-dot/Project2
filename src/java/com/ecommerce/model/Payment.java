package com.ecommerce.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Payment Model Class
 * Represents payment information for orders
 */
public class Payment {
    private int paymentId;
    private int orderId;
    private String paymentMethod;
    private BigDecimal amount;
    private String paymentStatus;
    private String transactionId;
    private Timestamp createdAt;
    
    // Payment method constants
    public static final String METHOD_CREDIT_CARD = "credit_card";
    public static final String METHOD_DEBIT_CARD = "debit_card";
    public static final String METHOD_PAYPAL = "paypal";
    public static final String METHOD_BANK_TRANSFER = "bank_transfer";
    public static final String METHOD_COD = "cod";
    
    // Payment status constants
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_FAILED = "failed";
    public static final String STATUS_REFUNDED = "refunded";
    
    public Payment() {}
    
    public Payment(int orderId, String paymentMethod, BigDecimal amount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = STATUS_PENDING;
    }
    
    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getFormattedAmount() {
        return String.format("₹%.2f", amount);
    }
    
    public String getMethodDisplay() {
        switch (paymentMethod) {
            case METHOD_CREDIT_CARD:
                return "Credit Card";
            case METHOD_DEBIT_CARD:
                return "Debit Card";
            case METHOD_PAYPAL:
                return "PayPal";
            case METHOD_BANK_TRANSFER:
                return "Bank Transfer";
            case METHOD_COD:
                return "Cash on Delivery";
            default:
                return "Unknown";
        }
    }
    
    public String getStatusDisplay() {
        return paymentStatus.substring(0, 1).toUpperCase() + paymentStatus.substring(1).toLowerCase();
    }
}