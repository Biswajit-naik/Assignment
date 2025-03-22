package com.expenses.tracker.ExpensesTracker.Exception;

public class JwtTokenException extends RuntimeException {
    public JwtTokenException(String message) {
        super(message);
    }
}
