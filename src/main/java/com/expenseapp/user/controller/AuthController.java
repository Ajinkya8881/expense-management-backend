package com.expenseapp.user.controller;

import com.expenseapp.common.response.ApiResponse;
import com.expenseapp.user.dto.AuthResponse;
import com.expenseapp.user.dto.LoginRequest;
import com.expenseapp.user.dto.RegisterRequest;
import com.expenseapp.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse authResponse = authService.register(request);

        ApiResponse<Object> response = ApiResponse.builder()
                .success(true)
                .message("User registered successfully")
                .data(authResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = authService.login(request);

        ApiResponse<Object> response = ApiResponse.builder()
                .success(true)
                .message("Login successful")
                .data(authResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
