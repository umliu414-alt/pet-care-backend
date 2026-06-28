package com.example.demo.dto.care_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CareRequestRequestDto {

    @NotNull(message = "Pet ID is mandatory")
    private Long petId;
    @NotBlank(message = "Care type is mandatory")
    private String careType;
    @NotNull(message = "Drop-off time is mandatory")
    private LocalDateTime dropOffTime;
    @NotNull(message = "Pickup time is mandatory")
    private LocalDateTime pickupTime;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal totalPayment;

    private String notes;

    public void setCareType(String careType) {
        this.careType = careType;
    }

    public void setDropOffTime(LocalDateTime dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public String getCareType() {
        return careType;
    }

    public LocalDateTime getDropOffTime() {
        return dropOffTime;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public String getNotes() {
        return notes;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

}
