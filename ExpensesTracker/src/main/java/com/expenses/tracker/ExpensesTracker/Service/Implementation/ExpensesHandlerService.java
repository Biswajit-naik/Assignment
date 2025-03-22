package com.expenses.tracker.ExpensesTracker.Service.Implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expenses.tracker.ExpensesTracker.DTO.MonthlyReport;
import com.expenses.tracker.ExpensesTracker.Model.Expensemodel;
import com.expenses.tracker.ExpensesTracker.Model.Usermodel;
import com.expenses.tracker.ExpensesTracker.Repository.ExpenseRepository;
import com.expenses.tracker.ExpensesTracker.Repository.UserRepository;

@Service
public class ExpensesHandlerService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ExpensesHandlerService.class);

    @Autowired
    public ExpensesHandlerService(ExpenseRepository serviceinstace, UserRepository userRepository) {
        this.expenseRepository = serviceinstace;
        this.userRepository = userRepository;

    }

    public Expensemodel createExpense(Expensemodel expense, Long userId) {
        Usermodel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not availble"));
        Expensemodel curexpense = new Expensemodel();
        curexpense.setAmmount(expense.getAmmount());
        curexpense.setCategory(expense.getCategory());
        curexpense.setDescription(expense.getDescription());
        curexpense.setDate(expense.getDate());
        curexpense.setUser(user);
        expenseRepository.save(curexpense);
        return curexpense;
    }

    public Page<Expensemodel> getUserExpenses(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Expensemodel> expensep = expenseRepository.findExpensesByUserId(id, pageable);
        return expensep;
    }

    public Expensemodel updateExpense(Long id, Expensemodel expense) {
        Expensemodel existuser = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not availble"));
        existuser.setAmmount(expense.getAmmount());
        existuser.setCategory(expense.getCategory());
        existuser.setDescription(expense.getDescription());
        existuser.setDate(expense.getDate());
        return existuser;
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public double calculateTotalExpenses(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Expensemodel> expenses = expenseRepository.findExpensesByUserAndDateRange(userId, startDate, endDate);
        logger.info("calculateTotalExpenses this method is worked: " + expenses);

        return expenses.stream().mapToDouble(Expensemodel::getAmmount).sum();
    }

    public Expensemodel getExpenseById(Long exid) {
        Optional<Expensemodel> e = expenseRepository.findByExpenseId(exid);
        return e.orElseThrow(() -> new RuntimeException("not found"));
    }

    public List<Object[]> getTotalAmountSpentByCategory(Long userId) {
        return expenseRepository.findTotalAmountSpentByCategory(userId);
    }

    public List<Object[]> getTotalAmountSpentByCategory(Long userId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findTotalAmountSpentByCategoryWithinDateRange(userId, startDate, endDate);
    }

    public MonthlyReport generateMonthlyReport(Long userId, String month, String year) {

        LocalDate startDate = LocalDate.parse(year + "-" + month + "-01");
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Object[]> categoryData = getTotalAmountSpentByCategory(userId, startDate, endDate);

        MonthlyReport report = new MonthlyReport();

        double totalExpenses = categoryData.stream()
                .mapToDouble(data -> (Double) data[1])
                .sum();

        report.setTotalExpenses(totalExpenses);

        List<MonthlyReport.CategoryBreakdown> breakdownList = categoryData.stream()
                .map(data -> new MonthlyReport.CategoryBreakdown((String) data[0], (Double) data[1]))
                .collect(Collectors.toList());

        report.setCategoryBreakdown(breakdownList);
        return report;
    }

}
