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
            * 
          FROM 
            dapil d 
          WHERE (
            LOWER(d.nama) LIKE LOWER(CONCAT('%', :nama,'%')) 
            OR LOWER(d.provinsi) LIKE LOWER(CONCAT('%', :provinsi,'%')) 
            OR d.jumlah_kursi = :jumlahKursi
          ) 
          AND 
          d.deleted_at IS NULL
  """, nativeQuery = true)
  Page<Dapil> findByFilter(@Param("nama") String nama, @Param("provinsi") String provinsi, @Param("jumlahKursi") Integer jumlahKursi, Pageable pagin);

  @Query(value = """
          SELECT 
            count(*) 
          FROM 
            dapil d 
          WHERE (
            LOWER(d.nama) LIKE LOWER(CONCAT('%', :nama,'%')) 
            OR LOWER(d.provinsi) LIKE LOWER(CONCAT('%', :provinsi,'%')) 
            OR d.jumlah_kursi = :jumlahKursi
          ) 
          AND 
          d.deleted_at IS NULL
  """, nativeQuery = true)
  Integer countByFilter(@Param("nama") String nama, @Param("provinsi") String provinsi, @Param("jumlahKursi") Integer jumlahKursi);
}
