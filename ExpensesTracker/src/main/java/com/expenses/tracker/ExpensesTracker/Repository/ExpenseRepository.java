package com.expenses.tracker.ExpensesTracker.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expenses.tracker.ExpensesTracker.Model.Expensemodel;

@Repository
public interface ExpenseRepository extends JpaRepository<Expensemodel, Long> {

   Optional<Expensemodel> findByExpenseId(Long expenseId);


   @Query("SELECT e FROM Expensemodel e WHERE e.user.userId = :userId AND e.date BETWEEN :startDate AND :endDate")
   List<Expensemodel> findExpensesByUserAndDateRange(@Param("userId") Long userId,
         @Param("startDate") LocalDate startDate,
         @Param("endDate") LocalDate endDate);

   @Query("SELECT e.category, SUM(e.ammount) FROM Expensemodel e WHERE e.user.userId = :userId AND e.date BETWEEN :startDate AND :endDate GROUP BY e.category")
   List<Object[]> findTotalAmountSpentByCategoryWithinDateRange(@Param("userId") Long userId,
         @Param("startDate") LocalDate startDate,
         @Param("endDate") LocalDate endDate);

   @Query("SELECT e.category, SUM(e.ammount) FROM Expensemodel e WHERE e.user.userId = :userId GROUP BY e.category")
   List<Object[]> findTotalAmountSpentByCategory(@Param("userId") Long userId);


   @Query(value = "SELECT e FROM Expensemodel e JOIN e.user u WHERE u.userId = :userId", countQuery = "SELECT count(e) FROM Expensemodel e JOIN e.user u WHERE u.userId = :userId", nativeQuery = false)
   Page<Expensemodel> findExpensesByUserId(@Param("userId") Long userId, Pageable pageable);
   
}