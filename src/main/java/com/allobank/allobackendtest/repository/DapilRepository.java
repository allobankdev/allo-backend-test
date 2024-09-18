package com.allobank.allobackendtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.entity.Dapil;

@Repository
public interface DapilRepository extends JpaRepository<Dapil, Long> {
}

