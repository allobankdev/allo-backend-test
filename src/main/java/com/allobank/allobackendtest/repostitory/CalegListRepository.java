package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalegListRepository extends JpaRepository<Caleg, UUID> {
    List<Caleg> findByDapilIdAndPartaiId(UUID dapilId, UUID partaiId, Pageable pageable);

    List<Caleg> findByDapilId(UUID dapilId, Pageable pageable);

    List<Caleg> findByPartaiId(UUID partaiId, Pageable pageable);
}