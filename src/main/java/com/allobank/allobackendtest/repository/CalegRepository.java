package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CalegRepository extends JpaRepository<Caleg, Integer> {
    Page<Caleg> findByDapilNamaDapilContainingAndPartaiNamaPartaiContaining(String namaDapil, String namaPartai, Pageable pageable);
}
