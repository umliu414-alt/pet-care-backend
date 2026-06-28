package com.example.demo.dto.user;

import com.example.demo.dto.pet.PetResponseDto;

import java.util.List;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        List<PetResponseDto> pets
) {
}
