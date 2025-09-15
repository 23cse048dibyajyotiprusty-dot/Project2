package com.ecommerce.util;

import com.ecommerce.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * User Authentication Filter
 * Ensures only authenticated users can access protected pages
 */
public class UserAuthFilter implements Filter {
    
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
        
        // Check if user is logged in
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        
        if (user != null) {
            // User is authenticated, continue with request
            chain.doFilter(request, response);
        } else {
            // Not authenticated, store requested URL and redirect to login
            String requestedURL = httpRequest.getRequestURL().toString();
            String queryString = httpRequest.getQueryString();
            
            if (queryString != null) {
                requestedURL += "?" + queryString;
            }
            
            // Store the requested URL in session for redirect after login
            HttpSession newSession = httpRequest.getSession(true);
            newSession.setAttribute("redirectURL", requestedURL);
            
            // Redirect to login page
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/login.jsp");
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup if needed
    }
}