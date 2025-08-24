package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.CalegEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing Caleg (Legislative Candidates) entities.
 */
@Repository
public interface CalegRepository extends JpaRepository<CalegEntity, UUID> {
    
    // Find Calegs by name
    List<CalegEntity> findByNama(String nama);
}
