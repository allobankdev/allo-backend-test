package com.allobankdev.splitbill.repository;

import com.allobankdev.splitbill.entity.BillGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillGroupRepository extends JpaRepository<BillGroup, String> {
}
