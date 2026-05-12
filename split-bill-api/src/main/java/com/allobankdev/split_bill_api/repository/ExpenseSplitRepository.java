package com.allobankdev.split_bill_api.repository;

import com.allobankdev.split_bill_api.entity.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseSplitRepository
        extends JpaRepository<ExpenseSplit, Long> {
}
