package com.example.demo.service;

import com.example.demo.dto.pet.PetResponseDto;
import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.demo.dto.user.UserResponseDto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto createUser(UserRequestDto dto) {
        UserEntity entity = mapToEntity(dto);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        UserEntity savedEntity = userRepository.save(entity);
        return mapToResponseDto(savedEntity);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return mapToResponseDto(entity);
    }

    private UserEntity mapToEntity(UserRequestDto dto) {
        UserEntity entity = new UserEntity();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());
        return entity;
    }

    private UserResponseDto mapToResponseDto(UserEntity entity) {
        List<PetResponseDto> petResponseDtos = entity.getPets()
                .stream()
                .map(pet -> new PetResponseDto(
                        pet.getId(),
                        pet.getName(),
                        pet.getType(),
                        entity.getId(),
                        entity.getName(),
                        pet.getCreatedAt(),
                        pet.getUpdatedAt()
                ))
                .toList();

        return new UserResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                petResponseDtos
        );
    }
}