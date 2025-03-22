package com.expenses.tracker.ExpensesTracker.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.expenses.tracker.ExpensesTracker.Model.Expensemodel;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.AuthenticationHandlerService;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.ExpensesHandlerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ExpensesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExpensesHandlerService expenseService;

    @Mock
    private AuthenticationHandlerService authenticationHandlerService;

    @InjectMocks
    private ExpensesController expensesController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(expensesController).build();
    }

    @Test
    void testCreateExpense() throws Exception {
        Expensemodel expense = new Expensemodel();
        expense.setAmmount(100.0);
        expense.setCategory("Food");
        expense.setDescription("Lunch");
        expense.setDate(LocalDate.of(2024, 3, 22));

        long userId = 1L;

        when(authenticationHandlerService.getAuthenticatedUserId()).thenReturn(userId);
        when(expenseService.createExpense(any(Expensemodel.class), eq(userId))).thenReturn(expense);

        mockMvc.perform(post("/api/expense")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ammount").value(100.0))
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.description").value("Lunch"));
    }

    @Test
    void testGetTotalExpenses() throws Exception {
        long userId = 1L;
        String startDate = "2024-03-01";
        String endDate = "2024-03-31";
        double total = 500.0;

        when(expenseService.calculateTotalExpenses(userId, LocalDate.parse(startDate), LocalDate.parse(endDate)))
                .thenReturn(total);

        mockMvc.perform(get("/api/expense/total")
                .param("userId", String.valueOf(userId))
                .param("startDate", startDate)
                .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(content().string("500.0"));
    }

    @Test
    void testDeleteExpense() throws Exception {
        long expenseId = 1L;

        doNothing().when(expenseService).deleteExpense(expenseId);

        mockMvc.perform(delete("/api/expense/{id}", expenseId))
                .andExpect(status().isNoContent());

        verify(expenseService, times(1)).deleteExpense(expenseId);
    }

    @Test
    void testGetTotalAmountByCategory() throws Exception {
        long userId = 1L;
        List<Object[]> categoryData = Collections.singletonList(new Object[]{"Food", 300.0});

        when(expenseService.getTotalAmountSpentByCategory(userId)).thenReturn(categoryData);

        mockMvc.perform(get("/api/expense/categories")
                .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0][0]").value("Food"))
                .andExpect(jsonPath("$[0][1]").value(300.0));
    }
}

