package com.allobank.allobackendtest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, String> {
    Page<Caleg> findByDapilAndPartai(Dapil dapil, Partai partai, Pageable pageable);
}
