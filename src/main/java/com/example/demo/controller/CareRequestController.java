package com.example.demo.controller;

import com.example.demo.dto.care_request.CareRequestRequestDto;
import com.example.demo.dto.care_request.CareRequestResponseDto;
import com.example.demo.service.CareRequestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/care-requests")
@Tag(name = "Care Requests", description = "Operations related to care requests")
public class CareRequestController {

    private final CareRequestService careRequestService;

    public CareRequestController(CareRequestService careRequestService) {
        this.careRequestService = careRequestService;
    }

    @PostMapping
    @Operation(summary = "Create care request", description = "Create a new care request")
    @ApiResponse(responseCode = "201", description = "Care request created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public CareRequestResponseDto createCareRequest(@Valid @RequestBody CareRequestRequestDto dto) {
        return careRequestService.createCareRequest(dto);
    }

    @GetMapping
    @Operation(summary = "Get all care requests", description = "Retrieve all care requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Care requests retrieved successfully")
    })
    public Page<CareRequestResponseDto> getAllCareRequests(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return careRequestService.getAllCareRequests(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get care request by ID", description = "Retrieve a care request by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Care request retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Care request not found")
    })
    public CareRequestResponseDto getCareRequestById(@PathVariable Long id) {
        return careRequestService.getCareRequestById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update care request", description = "Update an existing care request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Care request updated successfully"),
            @ApiResponse(responseCode = "404", description = "Care request not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public CareRequestResponseDto updateCareRequest(@PathVariable Long id,
                                                   @Valid @RequestBody CareRequestRequestDto dto) {
        return careRequestService.updateCareRequest(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete care request", description = "Delete an existing care request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Care request deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Care request not found")
    })
    public void deleteCareRequest(@PathVariable Long id) {
        careRequestService.deleteCareRequest(id);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update care request status", description = "Update the status of an existing care request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Care request status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Care request not found")
    })
    public CareRequestResponseDto updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return careRequestService.updateStatus(id, status);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get care requests by status", description = "Retrieve care requests by their status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Care requests retrieved successfully")
    })
    public List<CareRequestResponseDto> getCareRequestsByStatus(@PathVariable String status) {
        return careRequestService.getCareRequestsByStatus(status);
    }

    @GetMapping("/pet/{petId}")
    @Operation(summary = "Get care requests by pet ID", description = "Retrieve care requests by their pet ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Care requests retrieved successfully")
    })
    public List<CareRequestResponseDto> getCareRequestsByPetId(@PathVariable Long petId) {
        return careRequestService.getCareRequestsByPetId(petId);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get care requests by owner ID", description = "Retrieve care requests by their owner ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Care requests retrieved successfully")
    })
    public List<CareRequestResponseDto> getCareRequestsByOwnerId(@PathVariable Long ownerId) {
        return careRequestService.getCareRequestsByOwnerId(ownerId);
    }

}