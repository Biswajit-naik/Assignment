package com.expenses.tracker.ExpensesTracker.Controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expenses.tracker.ExpensesTracker.DTO.MonthlyReport;
import com.expenses.tracker.ExpensesTracker.Model.Expensemodel;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.AuthenticationHandlerService;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.ExpensesHandlerService;
import com.expenses.tracker.ExpensesTracker.utils.ExpensemodelAssembler;

@RestController
@RequestMapping("/api/expense")
public class ExpensesController {

    private static final Logger logger = LoggerFactory.getLogger(ExpensesController.class);

    @Autowired
    @Lazy
    private ExpensesHandlerService expenseService;

    @Autowired
    private AuthenticationHandlerService authenticationHandlerService;

    @Autowired
    private PagedResourcesAssembler<Expensemodel> pagedResourcesAssembler;

    @Autowired
    private ExpensemodelAssembler expensemodelAssembler;

    // need to get anywhere userid
    @PostMapping
    public ResponseEntity<Expensemodel> createExpense(@Valid @RequestBody Expensemodel expense) {
        long userid = authenticationHandlerService.getAuthenticatedUserId();
        Expensemodel createdExpense = expenseService.createExpense(expense, userid);
        return ResponseEntity.ok(createdExpense);
    }

    @GetMapping
    public ResponseEntity<?> getUserExpenses(@RequestParam Long userId,
            @RequestParam int page,
            @RequestParam int size) {
        Page<Expensemodel> expensePage = expenseService.getUserExpenses(userId, page, size);
        logger.info("Check the content in getUser expenses" + expensePage.getContent());
        PagedModel<EntityModel<Expensemodel>> res = pagedResourcesAssembler.toModel(expensePage, expensemodelAssembler);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expensemodel> updateExpense(@PathVariable Long id, @RequestBody Expensemodel expense) {
        return ResponseEntity.ok(expenseService.updateExpense(id, expense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalExpenses(@RequestParam Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        double total = expenseService.calculateTotalExpenses(userId,
                LocalDate.parse(startDate), LocalDate.parse(endDate));
        return ResponseEntity.ok(total);
    }

    @GetMapping("/expenses/{expenseId}")
    public ResponseEntity<EntityModel<Expensemodel>> getExpenseById(@PathVariable Long expenseId) {
        Expensemodel expense = expenseService.getExpenseById(expenseId);
        EntityModel<Expensemodel> entityModel = expensemodelAssembler.toModel(expense);
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Object[]>> getTotalAmountByCategory(@RequestParam Long userId) {
        return ResponseEntity.ok(expenseService.getTotalAmountSpentByCategory(userId));
    }

    @GetMapping("/report")
    public ResponseEntity<MonthlyReport> getMonthlyReport(@RequestParam("userid") Long userid,
            @RequestParam("month") String month,
            @RequestParam("year") String year) {
        logger.info("Excute the GetMonth;yRepoert" + userid + " " + month + " " + year);
        return ResponseEntity.ok(expenseService.generateMonthlyReport(userid, month, year));
    }
}
