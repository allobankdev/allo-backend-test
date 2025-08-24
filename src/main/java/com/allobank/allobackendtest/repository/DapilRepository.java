package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Dapil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DapilRepository extends JpaRepository<Dapil, UUID> {
    Page<Dapil> findAll(Pageable pageable);
    Boolean existsByNamaDapil(String namaDapil);
    Optional<Dapil> findByNamaDapil(String namaDapil);
}
