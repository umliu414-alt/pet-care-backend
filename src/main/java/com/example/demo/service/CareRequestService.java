package com.example.demo.service;

import com.example.demo.dto.care_request.CareRequestRequestDto;
import com.example.demo.dto.care_request.CareRequestResponseDto;
import com.example.demo.entity.CareRequestEntity;
import com.example.demo.entity.PetEntity;
import com.example.demo.exception.PetNotFoundException;
import com.example.demo.repository.CareRequestRepository;
import com.example.demo.repository.PetRepository;
import org.springframework.stereotype.Service;
import com.example.demo.exception.CareRequestNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CareRequestService {


    private static final Logger logger = LoggerFactory.getLogger(CareRequestService.class);
    private final CareRequestRepository careRequestRepository;
    private final PetRepository petRepository;

    public CareRequestService(CareRequestRepository careRequestRepository, PetRepository petRepository) {
        this.careRequestRepository = careRequestRepository;
        this.petRepository = petRepository;
    }


    private CareRequestResponseDto mapToDto(CareRequestEntity entity) {
        CareRequestResponseDto dto = new CareRequestResponseDto();

        dto.setId(entity.getId());

        dto.setPetId(entity.getPet().getId());
        dto.setPetName(entity.getPet().getName());
        dto.setPetType(entity.getPet().getType());

        dto.setOwnerName(entity.getPet().getOwner().getName());
        dto.setOwnerEmail(entity.getPet().getOwner().getEmail());
        dto.setOwnerPhone(entity.getPet().getOwner().getPhone());
        dto.setCareType(entity.getCareType());
        dto.setDropOffTime(entity.getDropOffTime());
        dto.setPickupTime(entity.getPickupTime());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setTotalPayment(entity.getTotalPayment());
        dto.setNotes(entity.getNotes());
        dto.setStatus(entity.getStatus().toString());

        return dto;
    }

    private CareRequestEntity mapToEntity(CareRequestRequestDto dto) {
        CareRequestEntity entity = new CareRequestEntity();

        PetEntity pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new PetNotFoundException(
                        "Pet not found with id: " + dto.getPetId()
                ));

        entity.setPet(pet);
        entity.setCareType(dto.getCareType());
        entity.setDropOffTime(dto.getDropOffTime());
        entity.setPickupTime(dto.getPickupTime());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setTotalPayment(dto.getTotalPayment());
        entity.setNotes(dto.getNotes());

        return entity;
    }

    public CareRequestResponseDto createCareRequest(CareRequestRequestDto dto) {
        logger.info("Creating care request for pet: {}", dto.getPetId());
        CareRequestEntity entity = mapToEntity(dto);
        CareRequestEntity savedEntity = careRequestRepository.save(entity);
        logger.info("Care request created successfully for pet: {}", savedEntity.getPet().getName());
        return mapToDto(savedEntity);
    }

    public List<CareRequestResponseDto> getAllCareRequests() {
        logger.info("Fetching all care requests");
        return careRequestRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CareRequestResponseDto getCareRequestById(Long id) {
        logger.info("Fetching care request with id: {}", id);
        CareRequestEntity entity = careRequestRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Care request not found with id: {}", id);
                    return new CareRequestNotFoundException("Care request not found with id: " + id);
                });

        return mapToDto(entity);
    }

    public CareRequestResponseDto updateCareRequest(Long id, CareRequestRequestDto dto) {

        logger.info("Updating care request with id: {}", id);
        CareRequestEntity existingEntity = careRequestRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Care request not found for update with id: {}", id);
                    return new CareRequestNotFoundException("Care request not found with id: " + id);
                });

        PetEntity pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new PetNotFoundException(
                        "Pet not found with id: " + dto.getPetId()
                ));

        existingEntity.setPet(pet);
        existingEntity.setCareType(dto.getCareType());
        existingEntity.setDropOffTime(dto.getDropOffTime());
        existingEntity.setPickupTime(dto.getPickupTime());
        existingEntity.setStartDate(dto.getStartDate());
        existingEntity.setEndDate(dto.getEndDate());
        existingEntity.setTotalPayment(dto.getTotalPayment());
        existingEntity.setNotes(dto.getNotes());

        CareRequestEntity updatedEntity = careRequestRepository.save(existingEntity);
        return mapToDto(updatedEntity);
    }

    public void deleteCareRequest(Long id) {
        logger.info("Deleting care request with id: {}", id);

        CareRequestEntity entity = careRequestRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Unable to delete care request, not found with id: {}", id);
                    return new CareRequestNotFoundException("Care request not found with id: " + id);
                });

        careRequestRepository.delete(entity);

        logger.info("Care request deleted successfully with id: {}", id);
    }

    public CareRequestResponseDto updateStatus(Long id, String status) {

        CareRequestEntity entity = careRequestRepository.findById(id)
                .orElseThrow(() -> new CareRequestNotFoundException(
                        "Care request not found with id: " + id
                ));

        CareRequestEntity.Status newStatus =
                Arrays.stream(CareRequestEntity.Status.values())
                        .filter(s -> s.name().equalsIgnoreCase(status))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid status: " + status
                                ));
        entity.setStatus(newStatus);

        CareRequestEntity updatedEntity = careRequestRepository.save(entity);

        return mapToDto(updatedEntity);
    }

    public List<CareRequestResponseDto> getCareRequestsByStatus(String status) {

        CareRequestEntity.Status requestStatus =
                Arrays.stream(CareRequestEntity.Status.values())
                        .filter(s -> s.name().equalsIgnoreCase(status))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid status: " + status
                                ));

        return careRequestRepository.findByStatus(requestStatus)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<CareRequestResponseDto> getCareRequestsByPetId(Long petId) {
        return careRequestRepository.findByPetId(petId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<CareRequestResponseDto> getCareRequestsByOwnerId(Long ownerId) {
        return careRequestRepository.findByPetOwnerId(ownerId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public Page<CareRequestResponseDto> getAllCareRequests(Pageable pageable) {
        return careRequestRepository.findAll(pageable)
                .map(this::mapToDto);
    }
}
