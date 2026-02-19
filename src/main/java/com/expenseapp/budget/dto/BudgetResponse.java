package com.expenseapp.budget.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BudgetResponse {

    private Long id;
    private Integer year;
    private Integer month;
    private BigDecimal amount;

}
