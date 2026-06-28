package com.example.demo.controller;

import com.example.demo.dto.auth.AuthLoginRequestDto;
import com.example.demo.dto.auth.AuthLoginResponseDto;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "Operations related to user authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthLoginResponseDto login(
            @Valid @RequestBody AuthLoginRequestDto dto) {

        return authService.login(dto);
    }
}