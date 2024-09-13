package com.allobank.allobackendtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
import com.allobank.allobackendtest.model.Caleg;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {
    List<Caleg> findByDapilId(UUID dapilId);
    List<Caleg> findByPartaiId(UUID partaiId);
    List<Caleg> findByDapilIdAndPartaiId(UUID dapilId, UUID partaiId);
}