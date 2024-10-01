package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {
    Page<Caleg> findByDapilIdAndPartaiId(UUID dapilId, UUID partaiId, Pageable pageable);

    Page<Caleg> findByDapilId(UUID dapilId, Pageable pageable);
    
    Page<Caleg> findByPartaiId(UUID partaiId, Pageable pageable);
}
