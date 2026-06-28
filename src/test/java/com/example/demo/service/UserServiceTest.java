package com.example.demo.service;

import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        UserEntity user = new UserEntity(
                11L,
                "Oliver",
                "Olier@test.com",
                "5152233376",
                "encodedPassword"
        );

        user.setPets(new ArrayList<>());

        when(userRepository.findById(11L)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getUserById(11L);

        assertNotNull(result);
        assertEquals(11L, result.id());
        assertEquals("Oliver", result.name());
        assertEquals("Olier@test.com", result.email());
        assertEquals("5152233376", result.phone());
        assertEquals(0, result.pets().size());

        verify(userRepository).findById(11L);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {

        UserEntity user1 = new UserEntity(
                11L,
                "Oliver",
                "oliver@test.com",
                "1111111111",
                "password1"
        );
        user1.setPets(new ArrayList<>());

        UserEntity user2 = new UserEntity(
                12L,
                "Lucy",
                "lucy@test.com",
                "2222222222",
                "password2"
        );
        user2.setPets(new ArrayList<>());

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<UserResponseDto> results =
                userService.getAllUsers();

        assertNotNull(results);
        assertEquals(2, results.size());

        assertEquals("Oliver", results.get(0).name());
        assertEquals("Lucy", results.get(1).name());

        verify(userRepository).findAll();
    }

    @Test
    void createUser_shouldEncodePasswordAndSaveUser() {
        UserRequestDto request = new UserRequestDto();
        request.setName("Oliver");
        request.setEmail("oliver@test.com");
        request.setPhone("5152233376");
        request.setPassword("Password8888");

        UserEntity savedUser = new UserEntity(
                11L,
                "Oliver",
                "oliver@test.com",
                "5152233376",
                "encodedPassword"
        );
        savedUser.setPets(new ArrayList<>());

        when(passwordEncoder.encode("Password8888"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(savedUser);

        UserResponseDto result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(11L, result.id());
        assertEquals("Oliver", result.name());
        assertEquals("oliver@test.com", result.email());
        assertEquals("5152233376", result.phone());

        verify(passwordEncoder).encode("Password8888");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getUserById(999L)
        );

        assertEquals(
                "User not found with id: 999",
                exception.getMessage()
        );

        verify(userRepository).findById(999L);
    }
}