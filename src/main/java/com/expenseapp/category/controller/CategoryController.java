package com.expenseapp.category.controller;

import com.expenseapp.category.dto.CategoryRequest;
import com.expenseapp.category.dto.CategoryResponse;
import com.expenseapp.category.service.CategoryService;
import com.expenseapp.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> create(
            @Valid @RequestBody CategoryRequest request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {

        String email = userDetails.getUsername();

        CategoryResponse response = categoryService.create(request, email);

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category created successfully")
                .data(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {

        String email = userDetails.getUsername();

        List<CategoryResponse> response = categoryService.getAll(email);

        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Categories fetched successfully")
                .data(response)
                .build();
    }
}
