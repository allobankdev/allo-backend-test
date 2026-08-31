package com.allobank.splitbill.repository;

import com.allobank.splitbill.domain.entity.BillGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillGroupRepository extends JpaRepository<BillGroup, Long> {
}
