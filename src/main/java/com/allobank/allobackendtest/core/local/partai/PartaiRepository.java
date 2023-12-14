package com.allobank.allobackendtest.core.local.partai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.core.local.partai.model.Partai;

@Repository
public interface PartaiRepository extends JpaRepository<Partai, Long> {
    
    Partai findOneByNamaPartai(String string);

}
