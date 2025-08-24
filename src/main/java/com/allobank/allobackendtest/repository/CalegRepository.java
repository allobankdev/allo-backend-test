package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.CalegEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for managing Caleg (Legislative Candidates) entities.
 */
@Repository
public interface CalegRepository extends JpaRepository<CalegEntity, UUID>, JpaSpecificationExecutor<CalegEntity> {
    
    // Find Calegs by name
    List<CalegEntity> findByNama(String nama);

    boolean existsByDapilIdAndNomorUrut(UUID dapilId, Integer nomorUrut);
}
