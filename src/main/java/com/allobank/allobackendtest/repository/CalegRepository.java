package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.CalegEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<CalegEntity, UUID> {

    List<CalegEntity> findByDapilEntity_NamaDapil(String namaDapil, Sort sort);

    List<CalegEntity> findByPartaiEntity_NamaPartai(String namaPartai, Sort sort);

    List<CalegEntity> findByDapilEntity_NamaDapilAndPartaiEntity_NamaPartai(String namaDapil, String namaPartai, Sort sort);

}
