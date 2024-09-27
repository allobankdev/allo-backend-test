package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID>, JpaSpecificationExecutor<Caleg> {
}
