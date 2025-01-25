package com.allobank.allobackendtest.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allobank.allobackendtest.model.Dapil;

public interface DapilRepository extends JpaRepository<Dapil, UUID> {

}
