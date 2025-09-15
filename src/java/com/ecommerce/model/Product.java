package com.ecommerce.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Product Model Class
 * Represents a product in the e-commerce application
 */
public class Product {
    private int productId;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private int categoryId;
    private String categoryName;
    private String imageUrl;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<String> additionalImages;
    private double averageRating;
    private int reviewCount;
    
    // Default constructor
    public Product() {
    }
    
    // Constructor with essential fields
    public Product(String name, String description, BigDecimal price, int stockQuantity, int categoryId, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.isActive = true;
    }
    
    // Constructor with all fields except timestamps
    public Product(int productId, String name, String description, BigDecimal price, int stockQuantity, 
                  int categoryId, String imageUrl, boolean isActive) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
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
    
    public List<String> getAdditionalImages() {
        return additionalImages;
    }
    
    public void setAdditionalImages(List<String> additionalImages) {
        this.additionalImages = additionalImages;
    }
    
    public double getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
    
    public int getReviewCount() {
        return reviewCount;
    }
    
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
    
    // Utility methods
    
    /**
     * Check if product is in stock
     * @return true if stock quantity > 0
     */
    public boolean isInStock() {
        return stockQuantity > 0;
    }
    
    /**
     * Check if product is low in stock (less than 10 items)
     * @return true if stock is low
     */
    public boolean isLowStock() {
        return stockQuantity > 0 && stockQuantity < 10;
    }
    
    /**
     * Get formatted price string
     * @return Price formatted as currency
     */
    public String getFormattedPrice() {
        return String.format("₹%.2f", price);
    }
    
    /**
     * Get short description (first 100 characters)
     * @return Truncated description
     */
    public String getShortDescription() {
        if (description == null) {
            return "";
        }
        return description.length() > 100 ? description.substring(0, 100) + "..." : description;
    }
    
    /**
     * Get stock status string
     * @return Stock status message
     */
    public String getStockStatus() {
        if (stockQuantity == 0) {
            return "Out of Stock";
        } else if (stockQuantity < 10) {
            return "Low Stock (" + stockQuantity + " left)";
        } else {
            return "In Stock";
        }
    }
    
    /**
     * Get rating stars as HTML string
     * @return HTML string with star rating
     */
    public String getRatingStars() {
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) averageRating;
        boolean hasHalfStar = (averageRating - fullStars) >= 0.5;
        
        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        if (hasHalfStar) {
            stars.append("☆");
        }
        
        int remaining = 5 - fullStars - (hasHalfStar ? 1 : 0);
        for (int i = 0; i < remaining; i++) {
            stars.append("☆");
        }
        
        return stars.toString();
    }
    
    /**
     * Calculate discount percentage if on sale
     * @param originalPrice Original price before discount
     * @return Discount percentage
     */
    public double getDiscountPercentage(BigDecimal originalPrice) {
        if (originalPrice == null || originalPrice.compareTo(price) <= 0) {
            return 0.0;
        }
        
        BigDecimal discount = originalPrice.subtract(price);
        return discount.divide(originalPrice, 4, BigDecimal.ROUND_HALF_UP)
                      .multiply(BigDecimal.valueOf(100))
                      .doubleValue();
    }
    
    @Override
    public String toString() {
        return "Product{" +
               "productId=" + productId +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", stockQuantity=" + stockQuantity +
               ", categoryName='" + categoryName + '\'' +
               ", isActive=" + isActive +
               ", averageRating=" + averageRating +
               ", reviewCount=" + reviewCount +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Product product = (Product) obj;
        return productId == product.productId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }
}