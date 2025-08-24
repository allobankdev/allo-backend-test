package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {
    Page<Caleg> findAll(Pageable pageable);
    Boolean existsByNama(String nama);
    Page<Caleg> findByPartaiNamaPartai(String namaPartai, Pageable pageable);
    Page<Caleg> findByDapilNamaDapil(String namaDapil, Pageable pageable);
    Page<Caleg> findByPartaiNamaPartaiAndDapilNamaDapil(String namaPartai, String namaDapil, Pageable pageable);
}
