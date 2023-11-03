package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.Caleg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, String>, JpaSpecificationExecutor<Caleg> {
}
