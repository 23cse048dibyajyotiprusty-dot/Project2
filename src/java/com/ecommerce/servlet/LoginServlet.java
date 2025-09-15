package com.ecommerce.servlet;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Login Servlet
 * Handles user authentication and session management
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet", "/login"})
public class LoginServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("index.html");
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        // Input validation
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            response.sendRedirect("login.jsp?error=1");
            return;
        }
        
        // Sanitize inputs
        email = SecurityUtil.sanitizeInput(email.trim().toLowerCase());
        
        // Validate email format
        if (!SecurityUtil.isValidEmail(email)) {
            response.sendRedirect("login.jsp?error=1");
            return;
        }
        
        // Basic rate limiting check (prevent brute force)
        if (!checkRateLimit(request, email)) {
            response.sendRedirect("login.jsp?error=4");
            return;
        }
        
        try {
            // Authenticate user
            User user = userDAO.authenticateUser(email, password);
            
            if (user != null) {
                // Authentication successful
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userEmail", user.getEmail());
                
                // Set session timeout (30 minutes)
                session.setMaxInactiveInterval(30 * 60);
                
                // Handle "Remember Me" functionality
                if ("on".equals(remember)) {
                    // Create remember me cookie (valid for 30 days)
                    String rememberToken = SecurityUtil.generateSessionToken();
                    Cookie rememberCookie = new Cookie("remember_token", rememberToken);
                    rememberCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                    rememberCookie.setHttpOnly(true);
                    rememberCookie.setSecure(request.isSecure());
                    rememberCookie.setPath("/");
                    response.addCookie(rememberCookie);
                    
                    // Store token in session for later validation
                    session.setAttribute("remember_token", rememberToken);
                }
                
                // Clear any previous failed login attempts
                clearFailedAttempts(request, email);
                
                // Log successful login
                logLoginAttempt(email, true, request.getRemoteAddr());
                
                // Redirect to dashboard or requested page
                String redirectURL = (String) session.getAttribute("redirectURL");
                if (redirectURL != null) {
                    session.removeAttribute("redirectURL");
                    response.sendRedirect(redirectURL);
                } else {
                    // Check if user or admin
                    if ("admin@ecommerce.com".equals(user.getEmail())) {
                        response.sendRedirect("admin/dashboard.jsp");
                    } else {
                        response.sendRedirect("dashboard.jsp");
                    }
                }
                
            } else {
                // Authentication failed
                recordFailedAttempt(request, email);
                logLoginAttempt(email, false, request.getRemoteAddr());
                response.sendRedirect("login.jsp?error=1");
            }
            
        } catch (Exception e) {
            // Log error
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            
            response.sendRedirect("login.jsp?error=5");
        }
    }
    
    /**
     * Check rate limiting to prevent brute force attacks
     * @param request HTTP request
     * @param email User email
     * @return true if within rate limit, false otherwise
     */
    private boolean checkRateLimit(HttpServletRequest request, String email) {
        HttpSession session = request.getSession(true);
        String key = "failed_attempts_" + email;
        Integer attempts = (Integer) session.getAttribute(key);
        
        if (attempts != null && attempts >= 5) {
            // Check if cooldown period has passed (15 minutes)
            Long lastAttempt = (Long) session.getAttribute(key + "_time");
            if (lastAttempt != null) {
                long timePassed = System.currentTimeMillis() - lastAttempt;
                if (timePassed < 15 * 60 * 1000) { // 15 minutes in milliseconds
                    return false;
                }
                // Reset attempts after cooldown
                session.removeAttribute(key);
                session.removeAttribute(key + "_time");
            }
        }
        
        return true;
    }
    
    /**
     * Record failed login attempt
     * @param request HTTP request
     * @param email User email
     */
    private void recordFailedAttempt(HttpServletRequest request, String email) {
        HttpSession session = request.getSession(true);
        String key = "failed_attempts_" + email;
        Integer attempts = (Integer) session.getAttribute(key);
        
        attempts = (attempts == null) ? 1 : attempts + 1;
        session.setAttribute(key, attempts);
        session.setAttribute(key + "_time", System.currentTimeMillis());
    }
    
    /**
     * Clear failed login attempts
     * @param request HTTP request
     * @param email User email
     */
    private void clearFailedAttempts(HttpServletRequest request, String email) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String key = "failed_attempts_" + email;
            session.removeAttribute(key);
            session.removeAttribute(key + "_time");
        }
    }
    
    /**
     * Log login attempt for security auditing
     * @param email User email
     * @param success Whether login was successful
     * @param ipAddress User's IP address
     */
    private void logLoginAttempt(String email, boolean success, String ipAddress) {
        // In a real application, this would write to a security log
        String status = success ? "SUCCESS" : "FAILED";
        String logMessage = String.format(
            "[%s] Login attempt: %s - Email: %s, IP: %s",
            new java.util.Date().toString(),
            status,
            email,
            ipAddress
        );
        
        System.out.println(logMessage);
        
        // TODO: Write to security audit log file or database
        // This could include additional information like user agent, session ID, etc.
    }
}