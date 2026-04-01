package com.edms.edms_backend.service.impl;

import com.edms.edms_backend.config.JwtService;
import com.edms.edms_backend.dto.request.LoginRequestDto;
import com.edms.edms_backend.dto.request.RegisterRequestDto;
import com.edms.edms_backend.dto.response.AuthResponseDto;
import com.edms.edms_backend.exception.ResourceAlreadyExistsException;
import com.edms.edms_backend.model.User;
import com.edms.edms_backend.model.enums.Role;
import com.edms.edms_backend.repository.UserRepository;
import com.edms.edms_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.EMPLOYEE)
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", user.getEmail());

        String token = jwtService.generateToken(user);

        return AuthResponseDto.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // This throws an exception automatically if credentials are wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);
        log.info("User logged in successfully: {}", user.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}