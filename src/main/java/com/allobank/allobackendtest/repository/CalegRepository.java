package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {
    
    @Query("SELECT c FROM Caleg c WHERE c.dapil.namaDapil = :namaDapil ORDER BY c.nomorUrut")
    List<Caleg> findByDapil(@Param("namaDapil") String namaDapil);

    @Query("SELECT c FROM Caleg c WHERE c.partai.namaPartai = :namaPartai ORDER BY c.nomorUrut")
    List<Caleg> findByPartai(@Param("namaPartai") String namaPartai);

}
