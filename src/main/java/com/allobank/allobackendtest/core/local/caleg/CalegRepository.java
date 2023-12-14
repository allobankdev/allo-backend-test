package com.allobank.allobackendtest.core.local.caleg;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.core.local.caleg.model.Caleg;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, Long> {

    Optional<Caleg> findOneByNama(String string);
    
}
