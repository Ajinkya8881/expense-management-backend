package com.expenseapp.expense.repository;

import java.math.BigDecimal;

public interface CategorySummaryProjection {

    String getCategoryName();
    BigDecimal getTotalAmount();
}
