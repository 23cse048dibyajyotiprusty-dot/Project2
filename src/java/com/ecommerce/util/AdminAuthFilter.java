package com.ecommerce.util;

import com.ecommerce.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Admin Authentication Filter
 * Ensures only authenticated admin users can access admin pages
 */
public class AdminAuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get current session
        HttpSession session = httpRequest.getSession(false);
        
        // Check if admin is logged in
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        
        // Validate admin access
        boolean isAdminAuthenticated = user != null && 
                                     "admin@ecommerce.com".equals(user.getEmail());
        
        if (isAdminAuthenticated) {
            // Admin is authenticated, continue with request
            chain.doFilter(request, response);
        } else {
            // Not authenticated, redirect to login
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/login.jsp");
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup if needed
    }
}