package com.example.demo.service;

import com.example.demo.dto.care_request.CareRequestRequestDto;
import com.example.demo.dto.care_request.CareRequestResponseDto;
import com.example.demo.entity.CareRequestEntity;
import com.example.demo.entity.PetEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.CareRequestNotFoundException;
import com.example.demo.repository.CareRequestRepository;
import com.example.demo.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CareRequestServiceTest {

    @Mock
    private CareRequestRepository careRequestRepository;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private CareRequestService careRequestService;

    @Test
    void getCareRequestById_shouldReturnCareRequest_whenExists() {
        UserEntity owner = new UserEntity(
                11L,
                "Oliver",
                "oliver@test.com",
                "5152233376",
                "password"
        );

        PetEntity pet = new PetEntity();
        pet.setId(1L);
        pet.setName("Buddy");
        pet.setType("Dog");
        pet.setOwner(owner);

        CareRequestEntity careRequest = new CareRequestEntity();
        careRequest.setId(100L);
        careRequest.setPet(pet);
        careRequest.setCareType("Boarding");
        careRequest.setDropOffTime(LocalDateTime.of(2026, 6, 20, 9, 0));
        careRequest.setPickupTime(LocalDateTime.of(2026, 6, 22, 17, 0));
        careRequest.setTotalPayment(new BigDecimal("120.00"));
        careRequest.setNotes("Needs morning walk");
        careRequest.setStatus(CareRequestEntity.Status.PENDING);

        when(careRequestRepository.findById(100L))
                .thenReturn(Optional.of(careRequest));

        CareRequestResponseDto result =
                careRequestService.getCareRequestById(100L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(1L, result.getPetId());
        assertEquals("Buddy", result.getPetName());
        assertEquals("Dog", result.getPetType());
        assertEquals("Oliver", result.getOwnerName());
        assertEquals("Boarding", result.getCareType());
        assertEquals("PENDING", result.getStatus());
        assertEquals(new BigDecimal("120.00"), result.getTotalPayment());

        verify(careRequestRepository).findById(100L);
    }

    @Test
    void getCareRequestById_shouldThrowException_whenRequestDoesNotExist() {

        when(careRequestRepository.findById(999L))
                .thenReturn(Optional.empty());

        CareRequestNotFoundException exception =
                assertThrows(
                        CareRequestNotFoundException.class,
                        () -> careRequestService.getCareRequestById(999L)
                );

        assertEquals(
                "Care request not found with id: 999",
                exception.getMessage()
        );

        verify(careRequestRepository).findById(999L);
    }

    @Test
    void createCareRequest_shouldCreateRequestSuccessfully() {

        CareRequestRequestDto request = new CareRequestRequestDto();
        request.setPetId(1L);
        request.setCareType("Boarding");
        request.setDropOffTime(
                LocalDateTime.of(2026, 6, 20, 9, 0)
        );
        request.setPickupTime(
                LocalDateTime.of(2026, 6, 22, 17, 0)
        );
        request.setTotalPayment(
                new BigDecimal("120.00")
        );
        request.setNotes("Needs morning walk");

        UserEntity owner = new UserEntity(
                11L,
                "Oliver",
                "oliver@test.com",
                "5152233376",
                "password"
        );

        PetEntity pet = new PetEntity();
        pet.setId(1L);
        pet.setName("Buddy");
        pet.setType("Dog");
        pet.setOwner(owner);

        CareRequestEntity savedRequest =
                new CareRequestEntity();

        savedRequest.setId(100L);
        savedRequest.setPet(pet);
        savedRequest.setCareType("Boarding");
        savedRequest.setDropOffTime(
                request.getDropOffTime()
        );
        savedRequest.setPickupTime(
                request.getPickupTime()
        );
        savedRequest.setTotalPayment(
                new BigDecimal("120.00")
        );
        savedRequest.setNotes("Needs morning walk");
        savedRequest.setStatus(
                CareRequestEntity.Status.PENDING
        );

        when(petRepository.findById(1L))
                .thenReturn(Optional.of(pet));

        when(careRequestRepository.save(
                any(CareRequestEntity.class)
        )).thenReturn(savedRequest);

        CareRequestResponseDto result =
                careRequestService.createCareRequest(
                        request
                );

        assertNotNull(result);

        assertEquals(100L, result.getId());
        assertEquals(1L, result.getPetId());
        assertEquals("Buddy", result.getPetName());
        assertEquals("Oliver", result.getOwnerName());
        assertEquals("Boarding", result.getCareType());
        assertEquals("PENDING", result.getStatus());

        verify(petRepository).findById(1L);
        verify(careRequestRepository)
                .save(any(CareRequestEntity.class));
    }

}