package com.expenses.tracker.ExpensesTracker.Controller;

import org.springframework.http.HttpHeaders;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.expenses.tracker.ExpensesTracker.DTO.LoginResponse;
import com.expenses.tracker.ExpensesTracker.DTO.LoginUser;
import com.expenses.tracker.ExpensesTracker.DTO.RegisterDetails;
import com.expenses.tracker.ExpensesTracker.Model.Usermodel;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.AuthenticationHandlerService;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.JwtHandlerService;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.UserHandlerService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserHandlerService userHandlerService;

    @Autowired
    private AuthenticationHandlerService authenticationHandlerService;

    @Autowired
    private JwtHandlerService jwtService;

    // singup
    @PostMapping("/register")
    public ResponseEntity<?> userregistration(@Valid @RequestBody RegisterDetails registerDetails) {
        try {
            Usermodel user = userHandlerService.createUser(registerDetails);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registeration Failed :" + e.getMessage());
        }
    }

    // signin
    @PostMapping(value = "/login")
    public ResponseEntity<?> userlogin(@Valid @RequestBody LoginUser user) {
        try {
            // send to authticate
            Authentication authentication = authenticationHandlerService.authenticateUser(user.getEmail(),
                    user.getPassword());

            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("failure", null, "Login failed"));
            }
            // genrate token
            String token = jwtService.genratetoken(user.getEmail(), true);

            HttpHeaders headers = createAuthHeaders(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            LoginResponse response = new LoginResponse("Sucess", token, "Login successful");

            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            logger.info("Error Occur " + e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    private HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
}
