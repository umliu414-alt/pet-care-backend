package com.example.demo.repository;

import com.example.demo.entity.CareRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareRequestRepository extends JpaRepository<CareRequestEntity, Long> {

    List<CareRequestEntity> findByStatus(CareRequestEntity.Status status);

    List<CareRequestEntity> findByPetId(Long petId);

    List<CareRequestEntity> findByPetOwnerId(Long ownerId);
}
