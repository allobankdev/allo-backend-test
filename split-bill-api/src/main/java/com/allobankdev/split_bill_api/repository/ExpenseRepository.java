package com.allobankdev.split_bill_api.repository;

import com.allobankdev.split_bill_api.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByGroupId(Long groupId);
}
