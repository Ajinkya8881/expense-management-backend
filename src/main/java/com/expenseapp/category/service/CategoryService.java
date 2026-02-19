package com.expenseapp.category.service;

import com.expenseapp.category.dto.CategoryRequest;
import com.expenseapp.category.dto.CategoryResponse;
import com.expenseapp.category.entity.Category;
import com.expenseapp.category.repository.CategoryRepository;
import com.expenseapp.common.exception.AppException;
import com.expenseapp.user.entity.User;
import com.expenseapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResponse create(CategoryRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found"));

        if (categoryRepository.existsByNameAndUser(request.getName(), user)) {
            throw new AppException("Category already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .user(user)
                .build();

        categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryResponse> getAll(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found"));

        return categoryRepository.findByUser(user)
                .stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .toList();
    }
}
