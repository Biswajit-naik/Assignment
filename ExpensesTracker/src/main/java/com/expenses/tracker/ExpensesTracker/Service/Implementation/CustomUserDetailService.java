package com.expenses.tracker.ExpensesTracker.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.expenses.tracker.ExpensesTracker.Model.CustomUsermodel;
import com.expenses.tracker.ExpensesTracker.Model.Usermodel;
import com.expenses.tracker.ExpensesTracker.Repository.UserRepository;

@Component
public class CustomUserDetailService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepo){
        this.userRepository = userRepo;
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("invoke loadUserByUsername method with parmeter :"+ username);
        Usermodel user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("username not found"));
        return new CustomUsermodel(user);
    }
    
}
