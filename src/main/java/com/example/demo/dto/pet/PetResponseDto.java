package com.example.demo.dto.pet;

import java.time.LocalDateTime;

public record PetResponseDto(
        Long id,
        String name,
        String type,
        Long ownerId,
        String ownerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}