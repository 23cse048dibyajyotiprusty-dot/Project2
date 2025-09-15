package com.ecommerce.servlet;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Register Servlet
 * Handles user registration and account creation
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet", "/register"})
public class RegisterServlet extends HttpServlet {
    
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
            response.sendRedirect("dashboard.jsp");
            return;
        }
        
        // Forward to registration page
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String terms = request.getParameter("terms");
        
        // Input validation
        if (firstName == null || firstName.trim().isEmpty() ||
            lastName == null || lastName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty() ||
            terms == null) {
            response.sendRedirect("register.jsp?error=1");
            return;
        }
        
        // Sanitize inputs
        firstName = SecurityUtil.sanitizeInput(firstName.trim());
        lastName = SecurityUtil.sanitizeInput(lastName.trim());
        email = SecurityUtil.sanitizeInput(email.trim().toLowerCase());
        phone = phone != null ? SecurityUtil.sanitizeInput(phone.trim()) : null;
        
        // Validate email format
        if (!SecurityUtil.isValidEmail(email)) {
            response.sendRedirect("register.jsp?error=4");
            return;
        }
        
        // Check password match
        if (!password.equals(confirmPassword)) {
            response.sendRedirect("register.jsp?error=3");
            return;
        }
        
        // Validate password strength
        if (!SecurityUtil.isValidPassword(password)) {
            response.sendRedirect("register.jsp?error=5");
            return;
        }
        
        // Validate phone number if provided
        if (phone != null && !phone.isEmpty() && !SecurityUtil.isValidPhone(phone)) {
            response.sendRedirect("register.jsp?error=6");
            return;
        }
        
        try {
            // Check if email already exists
            if (userDAO.emailExists(email)) {
                response.sendRedirect("register.jsp?error=2");
                return;
            }
            
            // Create user object
            String fullName = firstName + " " + lastName;
            String hashedPassword = SecurityUtil.hashPassword(password);
            User user = new User(fullName, email, hashedPassword);
            user.setPhone(phone);
            
            // Create user account
            int userId = userDAO.createUser(user);
            
            if (userId > 0) {
                // Registration successful
                logRegistrationAttempt(email, true, request.getRemoteAddr());
                response.sendRedirect("login.jsp?success=1");
            } else {
                // Registration failed
                logRegistrationAttempt(email, false, request.getRemoteAddr());
                response.sendRedirect("register.jsp?error=6");
            }
            
        } catch (Exception e) {
            // Log error
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            
            logRegistrationAttempt(email, false, request.getRemoteAddr());
            response.sendRedirect("register.jsp?error=6");
        }
    }
    
    /**
     * Log registration attempt for security auditing
     * @param email User email
     * @param success Whether registration was successful
     * @param ipAddress User's IP address
     */
    private void logRegistrationAttempt(String email, boolean success, String ipAddress) {
        String status = success ? "SUCCESS" : "FAILED";
        String logMessage = String.format(
            "[%s] Registration attempt: %s - Email: %s, IP: %s",
            new java.util.Date().toString(),
            status,
            email,
            ipAddress
        );
        
        System.out.println(logMessage);
        
        // TODO: Write to audit log file or database
    }
}