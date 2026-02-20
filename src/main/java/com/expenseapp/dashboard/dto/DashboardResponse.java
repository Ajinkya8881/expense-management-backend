package com.expenseapp.dashboard.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardResponse {

    private BigDecimal totalSpent;
    private BigDecimal budget;
    private BigDecimal remaining;
    private Long expenseCount;
    private String topCategory;
}
