package com.example.demo.controller;

import com.example.demo.dto.pet.PetRequestDto;
import com.example.demo.dto.pet.PetResponseDto;
import com.example.demo.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.service.JwtService;

@WebMvcTest(PetController.class)
@AutoConfigureMockMvc(addFilters = false)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void getAllPets_shouldReturnPets() throws Exception {

        PetResponseDto pet = new PetResponseDto(
                1L,
                "Buddy",
                "Dog",
                1L,
                "Larry",
                null,
                null
        );

        when(petService.getAllPets()).thenReturn(List.of(pet));

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Buddy"))
                .andExpect(jsonPath("$[0].type").value("Dog"))
                .andExpect(jsonPath("$[0].ownerName").value("Larry"));
    }

    @Test
    void getPetById_shouldReturnPet() throws Exception {


        PetResponseDto pet = new PetResponseDto(
                1L,
                "Buddy",
                "Dog",
                1L,
                "Larry",
                null,
                null
        );

        when(petService.getPetById(1L)).thenReturn(pet);

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.type").value("Dog"));
    }

    @Test
    void getPetsByOwnerId_shouldReturnPets() throws Exception {

        PetResponseDto pet = new PetResponseDto(
                1L,
                "Buddy",
                "Dog",
                1L,
                "Larry",
                null,
                null
        );

        when(petService.getPetsByOwnerId(1L))
                .thenReturn(List.of(pet));

        mockMvc.perform(get("/api/pets/owner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Buddy"))
                .andExpect(jsonPath("$[0].ownerId").value(1));
    }

    @Test
    void addPet_shouldReturnCreatedPet() throws Exception {

        PetRequestDto request = new PetRequestDto();
        request.setName("Buddy");
        request.setType("Dog");
        request.setOwnerId(1L);

        PetResponseDto response = new PetResponseDto(
                1L,
                "Buddy",
                "Dog",
                1L,
                "Larry",
                null,
                null
        );

        when(petService.addPet(any(PetRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.type").value("Dog"));
    }

    @Test
    void updatePet_shouldReturnUpdatedPet() throws Exception {

        PetRequestDto request = new PetRequestDto();
        request.setName("Buddy Updated");
        request.setType("Dog");
        request.setOwnerId(1L);


        PetResponseDto response = new PetResponseDto(
                1L,
                "Buddy",
                "Dog",
                1L,
                "Larry",
                null,
                null
        );

        when(petService.updatePet(eq(1L), any(PetRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Buddy"));
    }

    @Test
    void deletePet_shouldReturnOk() throws Exception {

        doNothing().when(petService).deletePet(1L);

        mockMvc.perform(delete("/api/pets/1"))
                .andExpect(status().isOk());
    }
}