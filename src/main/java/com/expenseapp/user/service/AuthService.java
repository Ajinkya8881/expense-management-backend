package com.expenseapp.user.service;


import com.expenseapp.common.exception.AppException;
import com.expenseapp.security.JwtService;
import com.expenseapp.user.dto.AuthResponse;
import com.expenseapp.user.dto.LoginRequest;
import com.expenseapp.user.dto.RegisterRequest;
import com.expenseapp.user.entity.User;
import com.expenseapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService  jwtService;

    public void register(RegisterRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("Email already exists"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new AppException("Invalid credentials");

        }
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
