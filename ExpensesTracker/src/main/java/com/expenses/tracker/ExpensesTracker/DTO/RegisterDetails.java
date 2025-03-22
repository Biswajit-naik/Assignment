package com.expenses.tracker.ExpensesTracker.DTO;

import javax.validation.constraints.*;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegisterDetails {

    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Email(message = "Email should be valid")
    @NotNull(message = "email cannot be null")
    private String email;

    @NotNull(message = "password cannot be null")
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

    private Boolean isactive = true;

    public RegisterDetails(
        @NotNull(message = "name cannot be null") @NotEmpty(message = "Name cannot be empty") String name,
        @Email(message = "Email should be valid") @NotNull(message = "email cannot be null") String email,
        @NotNull(message = "password cannot be null") @Size(min = 6, message = "Password should have at least 6 characters") String password,
        Boolean isactive) {
            
    this.name = name;
    this.email = email;
    this.password = password;
    this.isactive = isactive ? isactive : false;
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

}
