package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.WilayahDapil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WilayahDapilRepository extends JpaRepository<WilayahDapil, UUID> {
}
