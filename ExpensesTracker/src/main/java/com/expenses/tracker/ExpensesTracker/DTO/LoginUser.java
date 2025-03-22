package com.expenses.tracker.ExpensesTracker.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginUser {


    @NotBlank(message = "Email is required")
    @NotNull
    private String email;
    @NotBlank(message = "password is required")
    @NotNull
    private String password;

    public LoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
}
