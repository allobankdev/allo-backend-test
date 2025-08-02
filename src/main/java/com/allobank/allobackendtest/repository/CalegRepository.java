package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {

    @Query(
            value = """
        SELECT c
        FROM Caleg c
        WHERE COALESCE(:dapilId, c.dapil.id)  = c.dapil.id
          AND COALESCE(:partaiId, c.partai.id) = c.partai.id
        ORDER BY c.nomorUrut
      """,
            countQuery = """
        SELECT COUNT(c)
        FROM Caleg c
        WHERE COALESCE(:dapilId, c.dapil.id)  = c.dapil.id
          AND COALESCE(:partaiId, c.partai.id) = c.partai.id
      """
    )
    Page<Caleg> findAllWithFilter(
            @Param("dapilId")  UUID dapilId,
            @Param("partaiId") UUID partaiId,
            Pageable pageable
    );
}

