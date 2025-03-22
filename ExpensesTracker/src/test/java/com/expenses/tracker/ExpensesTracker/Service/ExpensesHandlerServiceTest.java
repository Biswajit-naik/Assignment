package com.expenses.tracker.ExpensesTracker.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.expenses.tracker.ExpensesTracker.Model.Expensemodel;
import com.expenses.tracker.ExpensesTracker.Model.Usermodel;
import com.expenses.tracker.ExpensesTracker.Repository.ExpenseRepository;
import com.expenses.tracker.ExpensesTracker.Repository.UserRepository;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.ExpensesHandlerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ExpensesHandlerServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpensesHandlerService expenseService;

    private Expensemodel expense;
    private Usermodel user;

    @BeforeEach
    void setUp() {
        user = new Usermodel();
        user.setUserId(1L);
        user.setName("Test User");

        expense = new Expensemodel();
        expense.setAmmount(200.0);
        expense.setCategory("Travel");
        expense.setDescription("Flight Ticket");
        expense.setDate(LocalDate.of(2024, 3, 22));
        expense.setUser(user);
    }

    @Test
    void testCreateExpense() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepository.save(any(Expensemodel.class))).thenReturn(expense);

        Expensemodel savedExpense = expenseService.createExpense(expense, 1L);

        assertNotNull(savedExpense);
        assertEquals(200.0, savedExpense.getAmmount());
        assertEquals("Travel", savedExpense.getCategory());
    }

    @Test
    void testCalculateTotalExpenses() {
        long userId = 1L;
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 31);

        List<Expensemodel> expenses = Arrays.asList(
                new Expensemodel(1L,100.0, "Food", "Lunch", startDate,user),
                new Expensemodel(2L,200.0, "Travel", "Flight", endDate, user));

        when(expenseRepository.findExpensesByUserAndDateRange(userId, startDate, endDate)).thenReturn(expenses);

        double total = expenseService.calculateTotalExpenses(userId, startDate, endDate);

        assertEquals(300.0, total);
    }

    @Test
    void testDeleteExpense() {
        doNothing().when(expenseRepository).deleteById(1L);

        assertDoesNotThrow(() -> expenseService.deleteExpense(1L));
        verify(expenseRepository, times(1)).deleteById(1L);
    }
}

