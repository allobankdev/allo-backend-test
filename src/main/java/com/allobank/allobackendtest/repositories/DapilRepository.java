package com.allobank.allobackendtest.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.entities.Dapil;

@Repository
public interface DapilRepository extends JpaRepository<Dapil, UUID> {
}
