package com.expenses.tracker.ExpensesTracker.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expenses.tracker.ExpensesTracker.Model.Usermodel;

@Repository
public interface UserRepository extends JpaRepository<Usermodel,Long> {

    Optional<Usermodel> findByEmail(String email);
    
} 