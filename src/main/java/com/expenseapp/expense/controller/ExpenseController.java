package com.expenseapp.expense.controller;

import com.expenseapp.common.response.ApiResponse;
import com.expenseapp.expense.dto.ExpenseRequest;
import com.expenseapp.expense.dto.ExpenseResponse;
import com.expenseapp.expense.dto.MonthlySummaryResponse;
import com.expenseapp.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ApiResponse<ExpenseResponse> create(
            @Valid @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User userDetails
    ) {

        ExpenseResponse response =
                expenseService.create(request, userDetails.getUsername());

        return ApiResponse.<ExpenseResponse>builder()
                .success(true)
                .message("Expense created successfully")
                .data(response)
                .build();
    }

    @GetMapping
    public ApiResponse<Page<ExpenseResponse>> getAll(
            @AuthenticationPrincipal User userDetails,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @PageableDefault(size = 10) Pageable pageable
    ) {

        Page<ExpenseResponse> response =
                expenseService.getAll(userDetails.getUsername(),
                        startDate,
                        endDate,
                        pageable);

        return ApiResponse.<Page<ExpenseResponse>>builder()
                .success(true)
                .message("Expenses fetched successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ExpenseResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User userDetails
    ){

        ExpenseResponse response =
                expenseService.getById(id, userDetails.getUsername());

        return ApiResponse.<ExpenseResponse>builder()
                .success(true)
                .message("Expense fetched successfully")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ExpenseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User userDetails
    ){

        ExpenseResponse response =
                expenseService.update(id, request, userDetails.getUsername());

        return ApiResponse.<ExpenseResponse>builder()
                .success(true)
                .message("Expense updated successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User userDetails
    ){

        expenseService.delete(id, userDetails.getUsername());

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Expense deleted successfully")
                .data(null)
                .build();
    }

    @GetMapping("/summary")
    public ApiResponse<MonthlySummaryResponse> getMonthlySummary(
            @AuthenticationPrincipal User userDetails,
            @RequestParam int year,
            @RequestParam int month
    ){
        MonthlySummaryResponse response =
                expenseService.getMonthlySummary(userDetails.getUsername(),
                        year,
                        month);

        return ApiResponse.<MonthlySummaryResponse>builder()
                .success(true)
                .message("Monthly summary fetched successfully")
                .data(response)
                .build();
    }
}
