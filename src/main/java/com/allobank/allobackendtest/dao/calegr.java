package com.allobank.allobackendtest.dao;

import java.util.UUID;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.entity.calege;
import com.allobank.allobackendtest.entity.dapile;
import com.allobank.allobackendtest.entity.partaie;

@Repository

public interface calegr extends JpaRepository<calege, UUID>{
    List<calege> findByDapilandPartaiId(String dapilId, String partaiId, Sort sort);
    List<calege> findByDapil(String dapil, Sort sort);
    List<calege> findByPartai(String partai, Sort sort);
    List<calege> orderByNomerUrut(Sort sort);
}

