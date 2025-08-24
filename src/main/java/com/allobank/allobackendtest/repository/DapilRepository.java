package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.DapilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing Dapil (Electoral District) entities.
 */
@Repository
public interface DapilRepository extends JpaRepository<DapilEntity, UUID> {
    // Find Dapil by name
    Optional<DapilEntity> findByNamaDapil(String namaDapil);
}