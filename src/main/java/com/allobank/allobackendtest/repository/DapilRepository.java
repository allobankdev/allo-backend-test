package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Dapil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DapilRepository extends JpaRepository<Dapil, UUID> {
  @Query(value = """
        SELECT
          id,
          nama,
          provinsi,
          jumlah_kursi,
          wilayah_dapil,
          created_at,
          updated_at,
          deleted_at
        FROM
          dapil
        WHERE
          deleted_at IS NULL
        AND (
            :nama IS NULL
          OR
            LOWER(nama) LIKE LOWER(CONCAT('%', :nama,'%'))
        )
        AND (
            :provinsi IS NULL
          OR
            LOWER(provinsi) LIKE LOWER(CONCAT('%', :provinsi,'%'))
        )
        AND (
            :jumlahKursi IS NULL
          OR
            jumlah_kursi = :jumlahKursi
        )
        """, nativeQuery = true)
  Page<Dapil> findByFilter(
          String nama,
          String provinsi,
          Integer jumlahKursi,
          Pageable pagin
  );

  @Query(value = """
        SELECT
          COUNT(id)
        FROM
          dapil
        WHERE
          deleted_at IS NULL
        AND (
            :nama IS NULL
          OR
            LOWER(nama) LIKE LOWER(CONCAT('%', :nama,'%'))
        )
        AND (
            :provinsi IS NULL
          OR
            LOWER(provinsi) LIKE LOWER(CONCAT('%', :provinsi,'%'))
        )
        AND (
            :jumlahKursi IS NULL
          OR
            jumlah_kursi = :jumlahKursi
        )
        """, nativeQuery = true)
  Integer countByFilter(
          String nama,
          String provinsi,
          Integer jumlahKursi
  );
}
