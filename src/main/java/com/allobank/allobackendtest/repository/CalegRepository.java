package com.allobank.allobackendtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.allobank.allobackendtest.entity.Caleg;

import java.util.List;

public interface CalegRepository extends JpaRepository<Caleg, Long> {

    @Query(value = "SELECT * FROM caleg c " +
        "WHERE (:dapilId IS NULL OR c.dapil_id = :dapilId) " +
        "AND (:partaiId IS NULL OR c.partai_id = :partaiId) " +
        "ORDER BY c.nomor_urut", 
        nativeQuery = true)
    List<Caleg> findByDapilAndPartaiOrdered(@Param("dapilId") Long dapilId, @Param("partaiId") Long partaiId);



}
