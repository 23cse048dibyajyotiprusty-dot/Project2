package com.ecommerce.model;

import java.sql.Timestamp;

/**
 * Address Model Class
 * Represents user addresses for shipping
 */
public class Address {
    private int addressId;
    private int userId;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
    private Timestamp createdAt;
    
    public Address() {}
    
    public Address(int userId, String streetAddress, String city, String state, String postalCode) {
        this.userId = userId;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = "India";
        this.isDefault = false;
    }
    
    // Getters and Setters
    public int getAddressId() { return addressId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getFullAddress() {
        return streetAddress + ", " + city + ", " + state + " " + postalCode + ", " + country;
    }
}