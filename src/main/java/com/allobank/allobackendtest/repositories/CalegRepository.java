package com.allobank.allobackendtest.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.entities.Caleg;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {
	List<Caleg> findByDapil_IdAndPartai_IdOrderByNomorUrut(UUID dapilId, UUID partaiId);
}
