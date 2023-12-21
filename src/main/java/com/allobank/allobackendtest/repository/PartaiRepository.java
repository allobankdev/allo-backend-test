package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Partai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartaiRepository extends JpaRepository<Partai, String> {
    List<Partai> findPartaiByNamaPartai(String nameCriteria);
}
