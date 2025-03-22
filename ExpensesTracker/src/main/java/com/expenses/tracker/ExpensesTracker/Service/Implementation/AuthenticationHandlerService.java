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
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof CustomUsermodel) {
            CustomUsermodel userDetails = (CustomUsermodel) authentication.getPrincipal();
            return userDetails.getUserId(); 
        }
        
        throw new RuntimeException("User not authenticated");
    }
}


