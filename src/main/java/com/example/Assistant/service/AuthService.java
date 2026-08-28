package com.example.Assistant.service;

import com.example.Assistant.dto.AuthResponse;
import com.example.Assistant.dto.LoginRequest;
import com.example.Assistant.dto.RegisterRequest;
import com.example.Assistant.entity.User;
import com.example.Assistant.exception.EmailAlreadyExistsException;
import com.example.Assistant.repository.UserRepository;
import com.example.Assistant.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered: " + request.getEmail());
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                // Plain password kabhi store nahi - hamesha hash karke
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);

        return new AuthResponse(token, saved.getEmail(), saved.getFullName());
    }

    public AuthResponse login(LoginRequest request) {
        // AuthenticationManager khud BCrypt se password match karega.
        // Galat hone pe ye khud BadCredentialsException throw karta hai -
        // hume manually check karne ki zaroorat nahi.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("User vanished after successful auth - should not happen"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getEmail(), user.getFullName());
    }
}