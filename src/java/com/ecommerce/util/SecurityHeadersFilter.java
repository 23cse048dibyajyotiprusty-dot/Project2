package com.ecommerce.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security Headers Filter
 * Adds security headers to all HTTP responses to enhance application security
 */
public class SecurityHeadersFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Add security headers
        addSecurityHeaders(httpRequest, httpResponse);
        
        // Continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup if needed
    }
    
    /**
     * Add security headers to the HTTP response
     * @param request HTTP request
     * @param response HTTP response
     */
    private void addSecurityHeaders(HttpServletRequest request, HttpServletResponse response) {
        
        // Prevent clickjacking attacks
        response.setHeader("X-Frame-Options", "DENY");
        
        // Prevent MIME type sniffing
        response.setHeader("X-Content-Type-Options", "nosniff");
        
        // Enable XSS protection in browsers
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        // Content Security Policy - helps prevent XSS attacks
        String csp = "default-src 'self'; " +
                    "script-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; " +
                    "style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; " +
                    "font-src 'self' https://cdnjs.cloudflare.com; " +
                    "img-src 'self' data: https:; " +
                    "connect-src 'self'; " +
                    "frame-src 'none'; " +
                    "object-src 'none';";
        response.setHeader("Content-Security-Policy", csp);
        
        // Referrer policy - controls how much referrer information is shared
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // Permissions policy - controls access to browser features
        response.setHeader("Permissions-Policy", 
            "geolocation=(), microphone=(), camera=(), payment=()");
        
        // Only add HSTS for HTTPS connections
        if (request.isSecure()) {
            // HTTP Strict Transport Security - force HTTPS
            response.setHeader("Strict-Transport-Security", 
                "max-age=31536000; includeSubDomains; preload");
        }
        
        // Cache control for sensitive pages
        String requestURI = request.getRequestURI();
        if (isSensitivePage(requestURI)) {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
        }
        
        // Remove server information
        response.setHeader("Server", "");
        
        // Add custom security header for application identification
        response.setHeader("X-Application", "ShopEasy-v1.0");
    }
    
    /**
     * Check if the requested page contains sensitive information
     * @param requestURI The request URI
     * @return true if the page is sensitive, false otherwise
     */
    private boolean isSensitivePage(String requestURI) {
        return requestURI.contains("/login") ||
               requestURI.contains("/register") ||
               requestURI.contains("/dashboard") ||
               requestURI.contains("/admin") ||
               requestURI.contains("/cart") ||
               requestURI.contains("/checkout") ||
               requestURI.contains("/orders") ||
               requestURI.contains("/profile");
    }
}