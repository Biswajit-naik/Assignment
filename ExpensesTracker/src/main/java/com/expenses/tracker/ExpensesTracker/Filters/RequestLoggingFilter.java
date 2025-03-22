package com.expenses.tracker.ExpensesTracker.Filters;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    // This is a placeholder for retrieving the user ID from the request
    // private String getUserIdFromRequest(HttpServletRequest request) {
    //     // Example of how to extract user ID from session or headers
    //     // You can modify this to suit your application’s authentication system
    //     String userId = (String) request.getSession().getAttribute("userId"); // Example from session
    //     if (userId == null) {
    //         userId = request.getHeader("userId"); // Or you could retrieve from headers if available
    //     }
    //     return userId != null ? userId : "Unknown";
    // }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Logging request details
        logger.info("Request Method: " + request.getMethod());
        logger.info("Request URL: " + request.getRequestURL());
        logger.info("Request IP: " + request.getRemoteAddr());
        
        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

}
