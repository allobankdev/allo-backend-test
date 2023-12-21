package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<CalegEntity, UUID> {

    List<CalegEntity> findByDapilAndPartai(DapilEntity dapil, PartaiEntity partai);

    List<CalegEntity> findByDapil(DapilEntity dapil);

    List<CalegEntity> findByPartai(PartaiEntity partai);


    List<CalegEntity> findByOrderByNomorUrut();
}
