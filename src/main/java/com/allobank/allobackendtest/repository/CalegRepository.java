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
                c.*
              FROM 
                caleg c 
              WHERE (
                    LOWER(c.nama) LIKE LOWER(CONCAT('%', :nama,'%')) 
                  OR
                    c.nomor_urut = :nomorUrut
                  OR
                    CAST(c.dapil_id as varchar(36)) = :dapil
                  OR
                    CAST(c.partai_id as varchar(36)) = :partai
              ) AND 
                c.deleted_at IS NULL
""", nativeQuery = true)
  Page<Caleg> findByFilter(
          @Param("nama") String nama,
          @Param("nomorUrut") Integer nomorUrut,
          @Param("dapil") String dapilId,
          @Param("partai") String partaiId,
          Pageable pagin
  );

  @Query(value = """
              SELECT 
                count(*)
              FROM 
                caleg c 
              WHERE (
                    LOWER(c.nama) LIKE LOWER(CONCAT('%', :nama,'%')) 
                  OR
                    c.nomor_urut = :nomorUrut
                  OR
                    CAST(c.dapil_id as varchar(36)) = :dapil
                  OR
                    CAST(c.partai_id as varchar(36)) = :partai
              ) AND 
                c.deleted_at IS NULL
""", nativeQuery = true)
  Integer countByFilter(
          @Param("nama") String nama,
          @Param("nomorUrut") Integer nomorUrut,
          @Param("dapil") String dapilId,
          @Param("partai") String partaiId
  );
}

