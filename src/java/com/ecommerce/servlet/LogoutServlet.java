package com.ecommerce.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Logout Servlet
 * Handles user logout and session cleanup
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/LogoutServlet", "/logout"})
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleLogout(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleLogout(request, response);
    }
    
    /**
     * Handle logout process
     * @param request HTTP request
     * @param response HTTP response
     * @throws IOException if redirect fails
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        try {
            // Get current session
            HttpSession session = request.getSession(false);
            
            if (session != null) {
                // Log logout attempt
                String userEmail = (String) session.getAttribute("userEmail");
                String userName = (String) session.getAttribute("userName");
                
                logLogoutAttempt(userEmail, userName, request.getRemoteAddr());
                
                // Clear all remember me cookies
                clearRememberMeCookies(request, response);
                
                // Invalidate session
                session.invalidate();
            }
            
            // Clear any browser caches
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            // Redirect to login page with success message
            response.sendRedirect("login.jsp?success=3");
            
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
            
            // Even if there's an error, redirect to home page
            response.sendRedirect("index.html");
        }
    }
    
    /**
     * Clear remember me cookies
     * @param request HTTP request
     * @param response HTTP response
     */
    private void clearRememberMeCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_token".equals(cookie.getName())) {
                    // Create a new cookie with same name but expired
                    Cookie expiredCookie = new Cookie("remember_token", "");
                    expiredCookie.setMaxAge(0); // Expire immediately
                    expiredCookie.setPath("/");
                    expiredCookie.setHttpOnly(true);
                    expiredCookie.setSecure(request.isSecure());
                    
                    response.addCookie(expiredCookie);
                    break;
                }
            }
        }
    }
    
    /**
     * Log logout attempt for security auditing
     * @param userEmail User's email
     * @param userName User's name
     * @param ipAddress User's IP address
     */
    private void logLogoutAttempt(String userEmail, String userName, String ipAddress) {
        String logMessage = String.format(
            "[%s] Logout: User: %s (%s), IP: %s",
            new java.util.Date().toString(),
            userName != null ? userName : "Unknown",
            userEmail != null ? userEmail : "Unknown",
            ipAddress
        );
        
        System.out.println(logMessage);
        
        // TODO: Write to security audit log file or database
        // This helps track user sessions and detect unusual logout patterns
    }
}