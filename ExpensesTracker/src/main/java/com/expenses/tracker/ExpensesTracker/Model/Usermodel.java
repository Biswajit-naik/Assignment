package com.expenses.tracker.ExpensesTracker.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Usermodel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",unique = true)
    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean active;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleSelector Role;
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void setRole(RoleSelector role) {
        Role = role;
    }
    

}
