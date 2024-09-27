package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Partai;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface PartaiRepository extends JpaRepository<Partai, Integer> {
}
