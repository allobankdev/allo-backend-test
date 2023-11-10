package com.allobank.allobackendtest.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Caleg;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID>, JpaSpecificationExecutor<Caleg> {

}
