package com.allobank.allobackendtest.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Partai;

@Repository
public interface PartaiRepository extends JpaRepository<Partai, UUID> {
    Partai findByNamaPartai(String namaPartai);
    Partai findByNomorUrut(Integer nomorUrut);
}