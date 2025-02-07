package com.allobank.allobackendtest.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Caleg;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {
  List<Caleg> findByDapilIdAndPartaiId(UUID dapilId, UUID partaiId);

  Page<Caleg> findAll(Pageable pageable);

  List<Caleg> findAll(Sort sort);

  @Query("SELECT c FROM Caleg c ORDER BY c.nomor_urut DESC")
  List<Caleg> findAllByOrderByNomorUrutDesc();

  @Query("SELECT c FROM Caleg c ORDER BY c.nomor_urut ASC")
  List<Caleg> findAllByOrderByNomorUrutAsc();
}
