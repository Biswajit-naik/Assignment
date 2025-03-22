package com.expenses.tracker.ExpensesTracker.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.expenses.tracker.ExpensesTracker.Model.CustomUsermodel;

@Service
public class AuthenticationHandlerService {
    
    private final AuthenticationManager authenticationManager;
    
    @Autowired
    public AuthenticationHandlerService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Authentication authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        return authentication.isAuthenticated() ? authentication : null;
    }

    public Long getAuthenticatedUserId() {
        // Retrieve the Authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Assuming the authenticated user has their userId stored in the principal or custom user details
        if (authentication != null && authentication.getPrincipal() instanceof CustomUsermodel) {
            CustomUsermodel userDetails = (CustomUsermodel) authentication.getPrincipal();
            return userDetails.getUserId();  // Assuming CustomUserDetails has getUserId method
        }
        
        // In case no authentication is found, throw an exception or return null
        throw new RuntimeException("User not authenticated");
    }
}


