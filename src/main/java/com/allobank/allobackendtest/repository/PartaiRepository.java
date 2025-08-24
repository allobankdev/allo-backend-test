package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.PartaiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * PartaiRepository interface for accessing Partai data.
 */
public interface PartaiRepository extends JpaRepository<PartaiEntity, UUID> {
    
    // Custom query method to find Partai by name
    Optional<PartaiEntity> findBynamaPartai(String namaPartai);
}
