package com.allobank.allobackendtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.entity.Partai;

@Repository
public interface PartaiRepository extends JpaRepository<Partai, Long> {
}


