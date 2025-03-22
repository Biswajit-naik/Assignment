package com.expenses.tracker.ExpensesTracker.DTO;

import java.util.List;

public class MonthlyReport {

    private double totalExpenses;
    private List<CategoryBreakdown> categoryBreakdown;

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public List<CategoryBreakdown> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategoryBreakdown> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public static class CategoryBreakdown {
        private String category;
        private double totalAmount;

        public CategoryBreakdown(String category, double totalAmount) {
            this.category = category;
            this.totalAmount = totalAmount;
        }

        // Getters and Setters
        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }
    }
}
