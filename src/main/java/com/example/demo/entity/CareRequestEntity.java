package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.example.demo.entity.PetEntity;


@Entity
@Table(name = "care_requests")
public class CareRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    private String careType;

    private LocalDateTime dropOffTime;
    private LocalDateTime pickupTime;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal totalPayment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(length = 2000)
    private String notes;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }

    public CareRequestEntity() {
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = Status.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public PetEntity getPet() {
        return pet;
    }

    public void setPet(PetEntity pet) {
        this.pet = pet;
    }


    public void setCareType(String careType) {
        this.careType = careType;
    }

    public void setDropOffTime(LocalDateTime dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
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

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}


