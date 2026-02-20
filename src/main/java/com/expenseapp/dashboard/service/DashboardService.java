package com.expenseapp.dashboard.service;


import com.expenseapp.budget.entity.Budget;
import com.expenseapp.budget.repository.BudgetRepository;
import com.expenseapp.common.exception.AppException;
import com.expenseapp.dashboard.dto.DashboardResponse;
import com.expenseapp.expense.dto.MonthlySummaryResponse;
import com.expenseapp.expense.service.ExpenseService;
import com.expenseapp.user.entity.User;
import com.expenseapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseService expenseService;
    private final BudgetRepository  budgetRepository;
    private final UserRepository userRepository;

    private User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new AppException("User not found"));
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(
            String email,
            int year,
            int month
    ){
        if (month < 1 || month > 12) {
            throw new AppException("Invalid month value");
        }

        User user = getUserByEmail(email);

        MonthlySummaryResponse summary = expenseService.getMonthlySummary(email,year,month);

        BigDecimal totalSpent = summary.getTotalAmount();
        Long expenseCount = summary.getTotalCount();

        Budget budget = budgetRepository.findByUserAndYearAndMonth(user,year,month)
                .orElseThrow(()-> new AppException("No budget configured for this month"));

        BigDecimal budgetAmount = budget.getAmount();

        BigDecimal remaining = budgetAmount.subtract(totalSpent);

        String topCategory = summary.getCategoryBreakdown().stream()
                .max((a,b) -> a.getTotalAmount()
                        .compareTo(b.getTotalAmount()))
                .map(MonthlySummaryResponse.CategoryBreakdown::getCategoryName)
                .orElse("No expenses");

        return DashboardResponse.builder()
                .totalSpent(totalSpent)
                .budget(budgetAmount)
                .remaining(remaining)
                .expenseCount(expenseCount)
                .topCategory(topCategory)
                .build();
    }
}
