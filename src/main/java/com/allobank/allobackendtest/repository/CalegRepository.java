package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {

  @Query(value = """
        SELECT
          id,
          dapil_id,
          partai_id,
          nomor_urut,
          nama,
          jenis_kelamin,
          created_at,
          updated_at,
          deleted_at
        FROM
          caleg
        WHERE
          deleted_at IS NULL
        AND (
            :nama IS NULL
          OR
            LOWER(nama) LIKE LOWER(CONCAT('%', :nama, '%'))
        )
        AND (
            :nomorUrut IS NULL
          OR
            nomor_urut = :nomorUrut
        )
        AND (
            :dapilId IS NULL
          OR
            CAST(dapil_id AS VARCHAR(36)) = :dapilId
        )
        AND (
            :partaiId IS NULL
          OR
            CAST(partai_id AS VARCHAR(36)) = :partaiId
        )
       """, nativeQuery = true)
  Page<Caleg> findByFilter(
          String nama,
          Integer nomorUrut,
          String dapilId,
          String partaiId,
          Pageable pagin
  );

  @Query(value = """
        SELECT
          COUNT(id)
        FROM
          caleg
        WHERE
          deleted_at IS NULL
        AND (
            :nama IS NULL
          OR
            LOWER(nama) LIKE LOWER(CONCAT('%', :nama, '%'))
        )
        AND (
            :nomorUrut IS NULL
          OR
            nomor_urut = :nomorUrut
        )
        AND (
            :dapilId IS NULL
          OR
            CAST(dapil_id AS VARCHAR(36)) = :dapilId
        )
        AND (
            :partaiId IS NULL
          OR
            CAST(partai_id AS VARCHAR(36)) = :partaiId
        )
        """, nativeQuery = true)
  Integer countByFilter(
          String nama,
          Integer nomorUrut,
          String dapilId,
          String partaiId
  );
}

