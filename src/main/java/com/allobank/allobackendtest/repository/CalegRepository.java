package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CalegRepository extends JpaRepository<Caleg, UUID> {
    List<Caleg> findByDapil_Id(UUID dapilId);
    List<Caleg> findByPartai_Id(UUID partaiId);
    List<Caleg> findByDapil_IdAndPartai_Id(UUID dapilId, UUID partaiId);
    List<Caleg> findByNomorUrutOrderByNomorUrutAsc(Integer nomorUrut);
}
