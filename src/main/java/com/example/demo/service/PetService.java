package com.example.demo.service;

import com.example.demo.dto.pet.PetRequestDto;
import com.example.demo.dto.pet.PetResponseDto;
import com.example.demo.entity.PetEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.PetRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.demo.exception.PetNotFoundException;
import com.example.demo.exception.UserNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public List<PetResponseDto> getAllPets() {
        logger.info("Fetching all pets");
        return petRepository.findAll()
                .stream()
                .map(this::convertToPetResponseDto)
                .collect(Collectors.toList());
    }

    public PetResponseDto getPetById(Long id) {
        logger.info("Fetching pet with ID: {}", id);
        return petRepository.findById(id)
                .map(this::convertToPetResponseDto)
                .orElseThrow(()-> { logger.warn("Pet not found with id {}", id);
                 return new PetNotFoundException("Pet not found with id: " + id); });
    }

    public PetResponseDto addPet(PetRequestDto dto) {
        logger.info("Creating pet with name: {}", dto.getName());
        UserEntity owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException(
                        "Owner not found with id: " + dto.getOwnerId()
                ));


        PetEntity entity = new PetEntity();
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setOwner(owner);

        PetEntity savedEntity = petRepository.save(entity);
        logger.info("Pet saved successfully with id {}", savedEntity.getId());
        return convertToPetResponseDto(savedEntity);
    }

    public PetResponseDto updatePet(Long id, PetRequestDto dto) {
        logger.info("Updating pet with ID: {}", id);

        PetEntity entity = petRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Unable to update the pet, pet not found with id {}", id);
                    return new PetNotFoundException("Pet not found with id: " + id);
                });

        UserEntity owner = userRepository.findById(dto.getOwnerId())
                            .orElseThrow(() -> new UserNotFoundException("Owner not found with id: " + dto.getOwnerId()
                ));

        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setOwner(owner);

        PetEntity updatedEntity = petRepository.save(entity);

        logger.info("Pet updated successfully with id {}", entity.getId());

        return convertToPetResponseDto(updatedEntity);
    }

    public List<PetResponseDto> getPetsByOwnerId(Long ownerId) {
        logger.info("Fetching pets for owner id {}", ownerId);

        userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Owner not found with id: " + ownerId
                ));

        return petRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::convertToPetResponseDto)
                .toList();
    }

    public void deletePet(Long id) {
        logger.info("Deleting pet with ID: {}", id);
        PetEntity entity = petRepository.findById(id)
                .orElseThrow(() ->
                        new PetNotFoundException("Pet not found with id: " + id));
        petRepository.delete(entity);
    }

    private PetResponseDto convertToPetResponseDto(PetEntity entity) {
        return new PetResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getOwner().getId(),
                entity.getOwner().getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
