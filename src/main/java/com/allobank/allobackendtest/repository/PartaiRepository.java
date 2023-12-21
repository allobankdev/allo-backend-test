package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.PartaiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartaiRepository extends JpaRepository<PartaiEntity, UUID> {
    Optional<PartaiEntity> findByNamaPartaiIgnoreCase(String partai);
}
