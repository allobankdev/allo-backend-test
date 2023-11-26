package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID>  {
    List<Caleg> findByPartai(Partai partai);

    List<Caleg> findByDapil(Dapil dapil);

}
