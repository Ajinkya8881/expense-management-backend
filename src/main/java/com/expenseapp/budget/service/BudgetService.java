package com.expenseapp.budget.service;


import com.expenseapp.budget.dto.BudgetRequest;
import com.expenseapp.budget.dto.BudgetResponse;
import com.expenseapp.budget.entity.Budget;
import com.expenseapp.budget.repository.BudgetRepository;
import com.expenseapp.common.exception.AppException;
import com.expenseapp.user.entity.User;
import com.expenseapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new AppException("User not found"));
    }

    @Transactional
    public BudgetResponse createOrUpdateBudget(BudgetRequest request,
                                               String email) {

        User  user = getUserByEmail(email);

        Budget budget = budgetRepository.findByUserAndYearAndMonth(
                user,
                request.getYear(),
                request.getMonth()
        )
                .orElse(null);

        if (budget == null) {

            budget = Budget.builder()
                    .year(request.getYear())
                    .month(request.getMonth())
                    .amount(request.getAmount())
                    .user(user)
                    .build();
        }else{
            budget.setAmount(request.getAmount());
        }
        budgetRepository.save(budget);

        return BudgetResponse.builder()
                .id(budget.getId())
                .year(request.getYear())
                .month(request.getMonth())
                .amount(request.getAmount())
                .build();
    }

    @Transactional(readOnly = true)
    public BudgetResponse getBudget(
            int year,
            int month,
            String email
    ){

        User user = getUserByEmail(email);

        Budget budget = budgetRepository
                .findByUserAndYearAndMonth(user, year,month)
                .orElseThrow(()-> new AppException("Budget not set for this month"));

        return BudgetResponse.builder()
                .id(budget.getId())
                .year(budget.getYear())
                .month(budget.getMonth())
                .amount(budget.getAmount())
                .build();
    }
}
