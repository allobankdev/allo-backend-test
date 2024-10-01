package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Dapil;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DapilRepository extends JpaRepository<Dapil, UUID> {
    
}
