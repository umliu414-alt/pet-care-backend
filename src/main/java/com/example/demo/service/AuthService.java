package com.example.demo.service;

import com.example.demo.dto.auth.AuthLoginRequestDto;
import com.example.demo.dto.auth.AuthLoginResponseDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public AuthLoginResponseDto login(AuthLoginRequestDto dto) {

        log.info("Login attempt for email {}", dto.getEmail());

        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password  ****"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.warn("Login failed for email {}", dto.getEmail());
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        log.info("Login successful for user {} (id={})",
                user.getEmail(),
                user.getId());

        return new AuthLoginResponseDto(
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}