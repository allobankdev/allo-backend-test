package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.dto.PartaiFilterDTO;
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
  @Query(value = "SELECT * FROM partai p WHERE (LOWER(p.nama) LIKE LOWER(CONCAT('%', :nama,'%')) OR p.nomor_urut = :nomorUrut) AND p.deleted_at IS NULL", nativeQuery = true)
  Page<Partai> findByFilter(@Param("nama") String nama, @Param("nomorUrut") Integer nomorUrut, Pageable pagin);

  @Query(value = "SELECT count(*) FROM partai p WHERE (LOWER(p.nama) LIKE LOWER(CONCAT('%', :nama,'%')) OR p.nomor_urut = :nomorUrut) AND p.deleted_at IS NULL", nativeQuery = true)
  Integer countByFilter(@Param("nama") String nama, @Param("nomorUrut") Integer nomorUrut);
}
