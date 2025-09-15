package com.ecommerce.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.regex.Pattern;

/**
 * Security Utility Class
 * Provides password hashing, validation, encryption/decryption, and input sanitization
 * Implements security best practices for e-commerce application
 */
public class SecurityUtil {
    
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String SECRET_KEY = "MySecretKey12345"; // In production, use environment variable
    
    // Regular expressions for input validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[1-9]?[0-9]{7,15}$"
    );
    
    /**
     * Hash password using SHA-256 with salt
     * @param password Plain text password
     * @return Hashed password with salt
     */
    public static String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            
            // Hash password
            byte[] hashedPassword = md.digest(password.getBytes());
            
            // Combine salt and hash
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);
            
            // Encode to Base64
            return Base64.getEncoder().encodeToString(combined);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }
    
    /**
     * Verify password against stored hash
     * @param password Plain text password
     * @param storedHash Stored hashed password
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Decode stored hash
            byte[] combined = Base64.getDecoder().decode(storedHash);
            
            // Extract salt (first 16 bytes)
            byte[] salt = new byte[16];
            System.arraycopy(combined, 0, salt, 0, salt.length);
            
            // Extract hash (remaining bytes)
            byte[] storedPasswordHash = new byte[combined.length - salt.length];
            System.arraycopy(combined, salt.length, storedPasswordHash, 0, storedPasswordHash.length);
            
            // Hash input password with same salt
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] inputPasswordHash = md.digest(password.getBytes());
            
            // Compare hashes
            return MessageDigest.isEqual(storedPasswordHash, inputPasswordHash);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Simple encryption for sensitive data
     * @param data Data to encrypt
     * @return Encrypted data encoded in Base64
     */
    public static String encrypt(String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
            
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    /**
     * Simple decryption for sensitive data
     * @param encryptedData Encrypted data in Base64
     * @return Decrypted data
     */
    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            
            return new String(decryptedData);
            
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    /**
     * Sanitize input to prevent XSS attacks
     * @param input User input string
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        return input.replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#x27;")
                   .replaceAll("/", "&#x2F;")
                   .replaceAll("&", "&amp;");
    }
    
    /**
     * Validate email format
     * @param email Email address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate password strength
     * @param password Password to validate
     * @return true if password meets criteria, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Validate phone number format
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Generate secure session token
     * @return Random session token
     */
    public static String generateSessionToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    /**
     * Validate SQL input to prevent SQL injection
     * @param input Input string to validate
     * @return true if safe, false if potentially dangerous
     */
    public static boolean isSqlSafe(String input) {
        if (input == null) {
            return true;
        }
        
        String[] dangerousKeywords = {
            "DROP", "DELETE", "INSERT", "UPDATE", "ALTER", "CREATE",
            "TRUNCATE", "EXEC", "EXECUTE", "UNION", "SELECT", "FROM",
            "--", "/*", "*/", "xp_", "sp_"
        };
        
        String upperInput = input.toUpperCase();
        for (String keyword : dangerousKeywords) {
            if (upperInput.contains(keyword)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Generate password reset token
     * @return Secure password reset token
     */
    public static String generatePasswordResetToken() {
        return generateSessionToken();
    }
    
    /**
     * Get password strength description
     * @param password Password to evaluate
     * @return String describing password strength
     */
    public static String getPasswordStrengthDescription(String password) {
        if (password == null || password.length() < 4) {
            return "Very Weak";
        }
        
        int score = 0;
        
        if (password.length() >= 8) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[@#$%^&+=].*")) score++;
        
        switch (score) {
            case 0:
            case 1:
                return "Very Weak";
            case 2:
                return "Weak";
            case 3:
                return "Fair";
            case 4:
                return "Good";
            case 5:
                return "Strong";
            default:
                return "Unknown";
        }
    }
}