package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.DapilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DapilRepository extends JpaRepository<DapilEntity, UUID> {

    Optional<DapilEntity> findByNamaDapilIgnoreCase(String dapil);


}
