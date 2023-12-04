package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.dto.PartaiFilterDTO;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartaiRepository extends JpaRepository<Partai, UUID> {
  @Query(value = """
        SELECT
          id,
          nama,
          nomor_urut,
          created_at,
          updated_at,
          deleted_at
        FROM
          partai
        WHERE
          deleted_at IS NULL
        AND (
            :nama IS NULL
          OR
            LOWER(nama) LIKE LOWER(CONCAT('%', :nama,'%'))
        )
        AND (
            :nomorUrut IS NULL
          OR
            nomor_urut = :nomorUrut
        )
        """, nativeQuery = true)
  Page<Partai> findByFilter(
          String nama,
          Integer nomorUrut,
          Pageable pagin
  );

  @Query(value = """
        SELECT
          COUNT(id)
        FROM
          partai
        WHERE
          deleted_at IS NULL
        AND (
            :nama IS NULL
          OR
            LOWER(nama) LIKE LOWER(CONCAT('%', :nama,'%'))
        )
        AND (
            :nomorUrut IS NULL
          OR
            nomor_urut = :nomorUrut
        )
        """, nativeQuery = true)
  Integer countByFilter(
          String nama,
          Integer nomorUrut
  );
}
