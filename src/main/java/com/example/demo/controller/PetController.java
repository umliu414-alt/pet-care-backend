package com.example.demo.controller;

import com.example.demo.dto.pet.PetResponseDto;
import com.example.demo.dto.pet.PetRequestDto;
import com.example.demo.service.PetService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/pets")
@Tag(name = "Pets", description = "Operations related to pets")
public class PetController {


    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all pets"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<PetResponseDto> getAllPets() {
        logger.info("Received request to get all pets");
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the pet"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @Operation(summary = "Get pet by ID", description = "Retrieve a pet by its ID")
    public PetResponseDto getPetById(@PathVariable Long id) {
        logger.info("Received request to get pet with ID: {}", id);
        return petService.getPetById(id);
    }

    @GetMapping("/owner/{ownerId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pets"),
            @ApiResponse(responseCode = "404", description = "Owner not found")
    })
    @Operation(summary = "Get pets by owner ID", description = "Retrieve pets by their owner's ID")
    public List<PetResponseDto> getPetsByOwnerId(@PathVariable Long ownerId) {
        return petService.getPetsByOwnerId(ownerId);
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @Operation(summary = "Create pet", description = "Create a new pet")
    public PetResponseDto addPet(@Valid @RequestBody PetRequestDto petRequestDto) {
        logger.info("Received request to create pet with name {}", petRequestDto.getName());
        return petService.addPet(petRequestDto);
    }

    @PutMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pet updated successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @Operation(summary = "Update pet", description = "Update an existing pet")
    public PetResponseDto updatePet(@PathVariable Long id,
                            @Valid @RequestBody PetRequestDto petRequestDto) {
        logger.info("Received request to update pet with id {}", id);
        return petService.updatePet(id, petRequestDto);
    }

    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pet deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @Operation(summary = "Delete pet", description = "Delete an existing pet")
    public void deletePet(@PathVariable Long id) {
        logger.info("Received request to delete pet with id {}", id);
        petService.deletePet(id);
    }

}