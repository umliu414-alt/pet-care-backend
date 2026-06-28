package com.example.demo.dto.auth;

public record AuthLoginResponseDto(
        String token,
        Long userId,
        String name,
        String email
) {
}