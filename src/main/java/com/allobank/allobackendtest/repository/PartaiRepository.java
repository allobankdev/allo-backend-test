package com.allobank.allobackendtest.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allobank.allobackendtest.model.Partai;

public interface PartaiRepository extends JpaRepository<Partai, String> {

}
