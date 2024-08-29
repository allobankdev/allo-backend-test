package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CalegRepository extends JpaRepository<Caleg, UUID> {

    @Query("SELECT c FROM Caleg c WHERE " +
            "(:namaDapil IS NULL OR c.dapil.namaDapil = :namaDapil) AND " +
            "(:namaPartai IS NULL OR c.partai.namaPartai = :namaPartai)")
    Page<Caleg> findByFilters(@Param("namaDapil") String namaDapil,
                              @Param("namaPartai") String namaPartai,
                              Pageable pageable);
}