package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CalegRepository extends JpaRepository<Caleg, String> {
    List<Caleg> findByDapilIdAndPartaiId(String dapilId, String partaiId, Sort sort);

    List<Caleg> findByDapilId(String dapilId, Sort sort);

    List<Caleg> findByPartaiId(String partaiId, Sort sort);

    List<Caleg> findAllByOrderByNomorUrut(Sort sort);
}

