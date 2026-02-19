package com.expenseapp.expense.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class MonthlySummaryResponse {

    private BigDecimal totalAmount;
    private Long totalCount;
    private List<CategoryBreakdown> categoryBreakdown;

    @Data
    @Builder
    public static class CategoryBreakdown {
        private String categoryName;
        private BigDecimal totalAmount;
    }
}
