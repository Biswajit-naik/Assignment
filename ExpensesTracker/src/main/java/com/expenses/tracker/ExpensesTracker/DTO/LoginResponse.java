package com.expenses.tracker.ExpensesTracker.DTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginResponse {
    private String status;
    private String token;
    private String message;

    public LoginResponse(String status, String token, String message) {
        this.status = status;
        this.token = token;
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
   
}
