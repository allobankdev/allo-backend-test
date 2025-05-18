package com.allobank.allobackendtest.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Caleg;

@Repository
public interface CalegRepo extends JpaRepository<Caleg, UUID> {
@Query("""
    SELECT c FROM Caleg c
    JOIN c.dapil d
    JOIN c.partai p
    WHERE (:dapil IS NULL OR d.namaDapil = :dapil)
      AND (:partai IS NULL OR p.namaPartai = :partai)
    ORDER BY c.nomorUrut ASC
""")
List<Caleg> findByDapilIdAndPartaiIdOrderByNomorUrutAsc(@Param("dapil") String dapil, @Param("partai") String partai);
}
