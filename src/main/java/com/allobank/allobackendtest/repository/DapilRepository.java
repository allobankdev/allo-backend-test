package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Dapil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DapilRepository extends JpaRepository<Dapil, UUID> {
}
