package com.expenses.tracker.ExpensesTracker.Service;
// package com.expenses.tracker.ExpensesTracker.Service.Implementation;

import com.expenses.tracker.ExpensesTracker.DTO.RegisterDetails;
import com.expenses.tracker.ExpensesTracker.Model.Usermodel;
import com.expenses.tracker.ExpensesTracker.Repository.UserRepository;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.UserHandlerService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserHandlerService userHandlerService;

    @Test
    void testCreateUser_Success() {
        RegisterDetails registerDetails = new RegisterDetails("Test User", "test@example.com", "password", true);

        when(userRepository.findByEmail(registerDetails.getEmail())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(registerDetails.getPassword())).thenReturn("encoded_password");

        Usermodel createdUser = userHandlerService.createUser(registerDetails);

        assertNotNull(createdUser);
        assertEquals("Test User", createdUser.getName());
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(Usermodel.class));
    }

    @Test
    void testCreateUser_EmailExists() {
        RegisterDetails registerDetails = new RegisterDetails("Test User", "test@example.com", "password", true);

        when(userRepository.findByEmail(registerDetails.getEmail())).thenReturn(java.util.Optional.of(new Usermodel()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userHandlerService.createUser(registerDetails);
        });

        assertEquals("Email already exists", exception.getMessage());
    }
}
