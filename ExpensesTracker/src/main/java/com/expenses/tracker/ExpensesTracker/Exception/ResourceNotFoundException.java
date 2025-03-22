package com.expenses.tracker.ExpensesTracker.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message.isEmpty() ? "Resource Not Found" : message);
    }
}
