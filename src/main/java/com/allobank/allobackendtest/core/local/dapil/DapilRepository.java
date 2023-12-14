package com.allobank.allobackendtest.core.local.dapil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.core.local.dapil.model.Dapil;

@Repository
public interface DapilRepository extends JpaRepository<Dapil, Long> {
    Dapil findOneByNamaDapil(String nama_dapil);
}
