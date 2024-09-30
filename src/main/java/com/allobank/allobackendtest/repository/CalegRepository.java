package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CalegRepository extends JpaRepository<Caleg, Long> {

    boolean existsByNomorUrut(Integer nomorUrut);


    @Query(value = "SELECT c.* " +
            "FROM caleg c " +
            "JOIN dapil d ON c.dapil_id = d.id " +
            "JOIN partai p ON c.partai_id = p.id " +
            "WHERE (:namaDapil IS NULL OR d.nama_dapil = :namaDapil) " +
            "AND (:namaPartai IS NULL OR p.nama_partai = :namaPartai)",
            nativeQuery = true)
    List<Caleg> findCalegByDapilNameAndPartaiNameWithoutSort(
            @Param("namaDapil") String namaDapil,
            @Param("namaPartai") String namaPartai);

    @Query(value = "SELECT c.* " +
            "FROM caleg c " +
            "JOIN dapil d ON c.dapil_id = d.id " +
            "JOIN partai p ON c.partai_id = p.id " +
            "WHERE (:namaDapil IS NULL OR d.nama_dapil = :namaDapil) " +
            "AND (:namaPartai IS NULL OR p.nama_partai = :namaPartai)",
            nativeQuery = true)
    Page<Caleg> findCalegWithSorting(
            @Param("namaDapil") String namaDapil,
            @Param("namaPartai") String namaPartai,
            Pageable pageable);

}


