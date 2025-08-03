package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CalegRepository extends JpaRepository<Caleg, Long> {

    List<Caleg> findByDapil_NamaDapilAndPartai_NamaPartai(String namaDapil, String namaPartai);
    List<Caleg> findByDapil_NamaDapil(String namaDapil);
    List<Caleg> findByPartai_NamaPartai(String namaPartai);
    List<Caleg> findByDapilId(String dapilId, Sort sort);
    List<Caleg> findByPartaiId(String partaiId, Sort sort);
    List<Caleg> findByDapilIdAndPartaiId(String dapilId, String partaiId, Sort sort);
    List<Caleg> findAll(Sort sort);



}
