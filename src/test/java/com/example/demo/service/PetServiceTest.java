package com.example.demo.service;

import com.example.demo.dto.pet.PetRequestDto;
import com.example.demo.dto.pet.PetResponseDto;
import com.example.demo.entity.PetEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.PetNotFoundException;
import com.example.demo.repository.PetRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PetService petService;

    @Test
    void getAllPets_shouldReturnAllPets() {
        UserEntity owner = new UserEntity(
                11L,
                "Oliver",
                "oliver@test.com",
                "5152233376",
                "password"
        );

        PetEntity pet1 = new PetEntity();
        pet1.setId(1L);
        pet1.setName("Buddy");
        pet1.setType("Dog");
        pet1.setOwner(owner);

        PetEntity pet2 = new PetEntity();
        pet2.setId(2L);
        pet2.setName("Lucy");
        pet2.setType("Cat");
        pet2.setOwner(owner);

        when(petRepository.findAll())
                .thenReturn(List.of(pet1, pet2));

        List<PetResponseDto> results = petService.getAllPets();

        assertNotNull(results);
        assertEquals(2, results.size());

        assertEquals("Buddy", results.get(0).name());
        assertEquals("Dog", results.get(0).type());
        assertEquals(11L, results.get(0).ownerId());
        assertEquals("Oliver", results.get(0).ownerName());

        assertEquals("Lucy", results.get(1).name());
        assertEquals("Cat", results.get(1).type());

        verify(petRepository).findAll();
    }


    @Test
    void addPet_shouldCreatePetSuccessfully() {

        PetRequestDto request = new PetRequestDto();
        request.setName("Buddy");
        request.setType("Dog");
        request.setOwnerId(11L);

        UserEntity owner = new UserEntity(
                11L,
                "Oliver",
                "oliver@test.com",
                "5152233376",
                "password"
        );

        PetEntity savedPet = new PetEntity();
        savedPet.setId(1L);
        savedPet.setName("Buddy");
        savedPet.setType("Dog");
        savedPet.setOwner(owner);

        when(userRepository.findById(11L))
                .thenReturn(Optional.of(owner));

        when(petRepository.save(any(PetEntity.class)))
                .thenReturn(savedPet);

        PetResponseDto result = petService.addPet(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Buddy", result.name());
        assertEquals("Dog", result.type());
        assertEquals(11L, result.ownerId());
        assertEquals("Oliver", result.ownerName());

        verify(userRepository).findById(11L);
        verify(petRepository).save(any(PetEntity.class));
    }


    @Test
    void getPetById_shouldReturnPet_whenPetExists() {
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

        when(petRepository.findById(1L))
                .thenReturn(Optional.of(pet));

        PetResponseDto result = petService.getPetById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Buddy", result.name());
        assertEquals("Dog", result.type());
        assertEquals(11L, result.ownerId());
        assertEquals("Oliver", result.ownerName());

        verify(petRepository).findById(1L);
    }


    @Test
    void getPetById_shouldThrowException_whenPetDoesNotExist() {
        when(petRepository.findById(999L))
                .thenReturn(Optional.empty());

        PetNotFoundException exception = assertThrows(
                PetNotFoundException.class,
                () -> petService.getPetById(999L)
        );

        assertEquals(
                "Pet not found with id: 999",
                exception.getMessage()
        );

        verify(petRepository).findById(999L);
    }

    @Test
    void updatePet_shouldUpdatePetSuccessfully() {
        PetRequestDto request = new PetRequestDto();
        request.setName("Buddy Updated");
        request.setType("Dog");
        request.setOwnerId(11L);

        UserEntity owner = new UserEntity(
                11L,
                "Oliver",
                "oliver@test.com",
                "5152233376",
                "password"
        );

        PetEntity existingPet = new PetEntity();
        existingPet.setId(1L);
        existingPet.setName("Buddy");
        existingPet.setType("Puppy");
        existingPet.setOwner(owner);

        PetEntity updatedPet = new PetEntity();
        updatedPet.setId(1L);
        updatedPet.setName("Buddy Updated");
        updatedPet.setType("Dog");
        updatedPet.setOwner(owner);

        when(petRepository.findById(1L))
                .thenReturn(Optional.of(existingPet));

        when(userRepository.findById(11L))
                .thenReturn(Optional.of(owner));

        when(petRepository.save(any(PetEntity.class)))
                .thenReturn(updatedPet);

        PetResponseDto result = petService.updatePet(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Buddy Updated", result.name());
        assertEquals("Dog", result.type());
        assertEquals(11L, result.ownerId());
        assertEquals("Oliver", result.ownerName());

        verify(petRepository).findById(1L);
        verify(userRepository).findById(11L);
        verify(petRepository).save(any(PetEntity.class));
    }

    @Test
    void updatePet_shouldThrowException_whenPetDoesNotExist() {

        PetRequestDto request = new PetRequestDto();
        request.setName("Buddy Updated");
        request.setType("Dog");
        request.setOwnerId(11L);

        PetNotFoundException exception = assertThrows(
                PetNotFoundException.class,
                () -> petService.updatePet(999L, request)
        );

        assertEquals(
                "Pet not found with id: 999",
                exception.getMessage()
        );

        verify(petRepository).findById(999L);

        verify(userRepository, never()).findById(anyLong());
        verify(petRepository, never()).save(any());
    }

    @Test
    void deletePet_shouldDeletePet_whenPetExists() {
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

        when(petRepository.findById(1L))
                .thenReturn(Optional.of(pet));

        petService.deletePet(1L);

        verify(petRepository).findById(1L);
        verify(petRepository).delete(pet);
    }

    @Test
    void deletePet_shouldThrowException_whenPetDoesNotExist() {
        when(petRepository.findById(999L))
                .thenReturn(Optional.empty());

        PetNotFoundException exception = assertThrows(
                PetNotFoundException.class,
                () -> petService.deletePet(999L)
        );

        assertEquals(
                "Pet not found with id: 999",
                exception.getMessage()
        );

        verify(petRepository).findById(999L);
        verify(petRepository, never()).deleteById(anyLong());
    }

}