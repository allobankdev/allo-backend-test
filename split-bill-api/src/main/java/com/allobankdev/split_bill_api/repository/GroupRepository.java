package com.allobankdev.split_bill_api.repository;

import com.allobankdev.split_bill_api.entity.BillGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<BillGroup, Long> {
}
