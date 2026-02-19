package com.expenseapp.budget.controller;


import com.expenseapp.budget.dto.BudgetRequest;
import com.expenseapp.budget.dto.BudgetResponse;
import com.expenseapp.budget.service.BudgetService;
import com.expenseapp.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;


@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ApiResponse<BudgetResponse> createOrUpdate(
            @Valid @RequestBody BudgetRequest request,
            @AuthenticationPrincipal User userDetails
    ){

        BudgetResponse response =
                budgetService.createOrUpdateBudget(request,
                        userDetails.getUsername());

        return ApiResponse.<BudgetResponse>builder()
                .success(true)
                .message("Budget saved successfully")
                .data(response)
                .build();
    }

    @GetMapping
    public ApiResponse<BudgetResponse> getBudget(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal User userDetails
    ){

        BudgetResponse response =
                budgetService.getBudget(
                        year,
                        month,
                        userDetails.getUsername()
                );

        return ApiResponse.<BudgetResponse>builder()
                .success(true)
                .message("Budget fetched successfully")
                .data(response)
                .build();
    }
}
