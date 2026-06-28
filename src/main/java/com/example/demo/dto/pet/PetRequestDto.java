package com.example.demo.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class PetRequestDto {


    private Long id;
    @NotBlank(message = "Pet name is mandatory")
    private String name;
    @NotBlank(message = "Pet type is mandatory")
    private String type;
    @NotNull(message = "Owner id is mandatory")
    private Long ownerId;

    public PetRequestDto() {
    }

    public PetRequestDto(Long id, String name, String type, Long ownerId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}