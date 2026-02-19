package com.expenseapp.budget.repository;

import com.expenseapp.budget.entity.Budget;
import com.expenseapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserAndYearAndMonth(
            User user,
            Integer year,
            Integer month
    );
}
