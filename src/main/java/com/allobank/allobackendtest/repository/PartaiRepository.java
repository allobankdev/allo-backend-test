package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Partai;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartaiRepository extends JpaRepository<Partai, UUID> {

}
