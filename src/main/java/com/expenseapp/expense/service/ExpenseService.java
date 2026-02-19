package com.expenseapp.expense.service;

import com.expenseapp.category.entity.Category;
import com.expenseapp.category.repository.CategoryRepository;
import com.expenseapp.common.exception.AppException;
import com.expenseapp.expense.dto.ExpenseRequest;
import com.expenseapp.expense.dto.ExpenseResponse;
import com.expenseapp.expense.dto.MonthlySummaryResponse;
import com.expenseapp.expense.entity.Expense;
import com.expenseapp.expense.repository.CategorySummaryProjection;
import com.expenseapp.expense.repository.ExpenseRepository;
import com.expenseapp.user.entity.User;
import com.expenseapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found"));
    }

    @Transactional
    public ExpenseResponse create(ExpenseRequest request, String email) {

        User user = getUserByEmail(email);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AppException("You cannot use this category");
        }

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .expenseDate(request.getExpenseDate())
                .category(category)
                .user(user)
                .build();

        expenseRepository.save(expense);

        return mapToResponse(expense);
    }

    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getAll(String email,
                                        LocalDate startDate,
                                        LocalDate endDate,
                                        Pageable pageable) {

        User user = getUserByEmail(email);

        if( startDate != null && endDate != null && startDate.isAfter(endDate) ) {
            throw new AppException("Start date cannot be after end date");
        }

        Specification<Expense> spec = (root, query, cb) ->
                cb.equal(root.get("user"), user);

        if( startDate != null ) {
            spec = spec.and((root,query,cb) ->
                    cb.greaterThanOrEqualTo(root.get("expenseDate"), startDate));
        }
        if( endDate != null ) {
            spec = spec.and((root,query,cb)->
                    cb.lessThanOrEqualTo(root.get("expenseDate"), endDate));
        }

        return expenseRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ExpenseResponse getById(Long id, String email) {

        User user = getUserByEmail(email);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new AppException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new AppException("You cannot access this expense");
        }

        return mapToResponse(expense);
    }

    @Transactional
    public ExpenseResponse update(Long id, ExpenseRequest request, String email) {

        User user = getUserByEmail(email);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new AppException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new AppException("You cannot update this expense");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AppException("You cannot use this category");
        }

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setCategory(category);

        return mapToResponse(expense);
    }

    @Transactional
    public void delete(Long id, String email) {

        User user = getUserByEmail(email);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new AppException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new AppException("You cannot delete this expense");
        }

        expenseRepository.delete(expense);
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .expenseDate(expense.getExpenseDate())
                .categoryId(expense.getCategory().getId())
                .categoryName(expense.getCategory().getName())
                .build();
    }

    @Transactional(readOnly = true)
    public MonthlySummaryResponse getMonthlySummary(
            String email,
            int year,
            int month
    ){
        User user = getUserByEmail(email);

        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Expense> expenses =
                expenseRepository.findAll((root,query, cb) ->
                        cb.and(
                                cb.equal(root.get("user"), user),
                                cb.between(root.get("expenseDate"), startDate, endDate)
                        )
                );

        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalCount = Long.valueOf(expenses.size());

        List<CategorySummaryProjection> categorySummary =
                expenseRepository.getCategorySummary(user, startDate, endDate);

        List<MonthlySummaryResponse.CategoryBreakdown> breakdown =

                categorySummary.stream()
                        .map(p->MonthlySummaryResponse.CategoryBreakdown.builder()
                                .categoryName(p.getCategoryName())
                                .totalAmount(p.getTotalAmount())
                                .build())
                        .toList();


        return MonthlySummaryResponse.builder()
                .totalAmount(totalAmount)
                .totalCount(totalCount)
                .categoryBreakdown(breakdown)
                .build();
    }
}
