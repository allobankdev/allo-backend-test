package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, Long> {

}
