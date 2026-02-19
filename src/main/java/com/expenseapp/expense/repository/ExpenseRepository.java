package com.expenseapp.expense.repository;

import com.expenseapp.expense.entity.Expense;
import com.expenseapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long>,
        JpaSpecificationExecutor<Expense> {

    @Query("""
    SELECT c.name AS categoryName,
           SUM(e.amount) AS totalAmount
    FROM Expense e
    JOIN e.category c
    WHERE e.user = :user
      AND e.expenseDate BETWEEN :startDate AND :endDate
    GROUP BY c.name
""")
    List<CategorySummaryProjection> getCategorySummary(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


}
