package com.expenses.tracker.ExpensesTracker.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expenses.tracker.ExpensesTracker.DTO.RegisterDetails;
import com.expenses.tracker.ExpensesTracker.Exception.ResourceNotFoundException;
import com.expenses.tracker.ExpensesTracker.Model.RoleSelector;
import com.expenses.tracker.ExpensesTracker.Model.Usermodel;
import com.expenses.tracker.ExpensesTracker.Repository.UserRepository;

@Service
public class UserHandlerService {
    
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserHandlerService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder= passwordEncoder;
    }

    public Usermodel createUser(RegisterDetails user){
        String encodepassword = passwordEncoder.encode(user.getPassword());

        userRepository.findByEmail(user.getEmail())
            .ifPresent(existingUser -> {
                throw new RuntimeException("Email already exists");
            });
        
        Usermodel curruser = new Usermodel();
        curruser.setName(user.getName());
        curruser.setEmail(user.getEmail());
        curruser.setPassword(encodepassword);
        curruser.setActive(true);
        curruser.setRole(RoleSelector.LITEUSER);
        userRepository.save(curruser);
        return curruser;
    }

    public Usermodel updateUser(Long userId, Usermodel updatedUser) throws ResourceNotFoundException {
        return userRepository.findById(userId)
            .map(user -> {
                user.setName(updatedUser.getName());
                user.setEmail(updatedUser.getEmail());
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                user.setRole(updatedUser.getRole());
                return userRepository.save(user);
            }).orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));
    }

    public void deleteuser(Long userid){
        userRepository.deleteById(userid);
    }
}
