package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Partai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PartaiRepository extends JpaRepository<Partai, Long> {

    Optional<Partai> findById(Long id);

    boolean existsByNamaPartai(String namaPartai);

    List<Partai> findByNamaPartaiIgnoreCase(String namaPartai);

}
