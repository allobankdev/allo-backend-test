package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Dapil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DapilRepository extends JpaRepository<Dapil, UUID> {
}
