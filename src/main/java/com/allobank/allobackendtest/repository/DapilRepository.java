package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Dapil;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface DapilRepository extends JpaRepository<Dapil, Integer> {

}
