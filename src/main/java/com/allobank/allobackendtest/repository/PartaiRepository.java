package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Partai;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PartaiRepository extends JpaRepository<Partai, UUID> {
}
