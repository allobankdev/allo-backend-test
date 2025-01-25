package com.allobank.allobackendtest.repositories;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.allobank.allobackendtest.model.Caleg;

public interface CalegRepository extends JpaRepository<Caleg, UUID> {
    @Query(value = "select distinct c.*, d.nama_dapil, p.nama_partai FROM caleg c " +
            "join partai p on p.id = c.partai_id " +
            "join dapil d on d.id = c.dapil_id " +
            "join wilayah_dapil wd on d.id = wd.dapil_id " +
            "WHERE (:partai IS NULL OR p.nama_partai ILIKE CONCAT('%', :partai, '%')) " +
            "AND (:dapil IS NULL OR d.nama_dapil ILIKE CONCAT('%', :dapil, '%'))", nativeQuery = true)
    Page<Map<String, Object>> searchCaleg(Pageable pageable, @Param("partai") String partai,
            @Param("dapil") String dapil);
}
