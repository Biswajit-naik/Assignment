package com.expenses.tracker.ExpensesTracker.Controller;

import com.expenses.tracker.ExpensesTracker.DTO.LoginResponse;
import com.expenses.tracker.ExpensesTracker.DTO.LoginUser;
import com.expenses.tracker.ExpensesTracker.DTO.RegisterDetails;
import com.expenses.tracker.ExpensesTracker.Model.Usermodel;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.AuthenticationHandlerService;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.JwtHandlerService;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.UserHandlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AuthControllerTests {

    private MockMvc mockMvc;

    @Mock
    private UserHandlerService userHandlerService;

    @Mock
    private AuthenticationHandlerService authenticationHandlerService;

    @Mock
    private JwtHandlerService jwtService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testUserRegistration() throws Exception {
        // Prepare test data
        RegisterDetails registerDetails = new RegisterDetails("Test User", "test@example.com", "password",true);

        // Mock the userHandlerService.createUser method
        Usermodel mockUser = new Usermodel();
        mockUser.setEmail("test@example.com");
        mockUser.setName("Test User");

        when(userHandlerService.createUser(any(RegisterDetails.class))).thenReturn(mockUser);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{ \"name\": \"Test User\", \"email\": \"test@example.com\", \"password\": \"password\" }"))
                .andExpect(status().isOk());

        verify(userHandlerService, times(1)).createUser(any(RegisterDetails.class));
    }

    @Test
    void testUserLogin() throws Exception {
        // Prepare test data
        LoginUser loginUser = new LoginUser("test@example.com", "password");

        // Mock authentication and JWT generation
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationHandlerService.authenticateUser(anyString(), anyString())).thenReturn(mockAuth);
        when(jwtService.genratetoken(anyString(), anyBoolean())).thenReturn("mock_token");

        // Perform the POST request and validate the response
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{ \"username\": \"test@example.com\", \"password\": \"password\" }"))
                .andExpect(status().isOk());

        verify(authenticationHandlerService, times(1)).authenticateUser(anyString(), anyString());
        verify(jwtService, times(1)).genratetoken(anyString(), anyBoolean());
    }
}

