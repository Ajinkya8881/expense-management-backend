package com.expenseapp.expense.dto;


import com.expenseapp.category.entity.Category;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ExpenseResponse {

    private Long id;
    private String title;
    private BigDecimal amount;
    private LocalDate expenseDate;

    private Long categoryId;
    private String categoryName;
}
