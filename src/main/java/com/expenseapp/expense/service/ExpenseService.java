package com.expenseapp.expense.service;

import com.expenseapp.category.entity.Category;
import com.expenseapp.category.repository.CategoryRepository;
import com.expenseapp.common.exception.AppException;
import com.expenseapp.expense.dto.ExpenseRequest;
import com.expenseapp.expense.dto.ExpenseResponse;
import com.expenseapp.expense.entity.Expense;
import com.expenseapp.expense.repository.ExpenseRepository;
import com.expenseapp.user.entity.User;
import com.expenseapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<ExpenseResponse> getAll(String email, Pageable pageable) {

        User user = getUserByEmail(email);

        return expenseRepository.findByUser(user, pageable)
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
}
