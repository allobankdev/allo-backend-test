package com.allobankdev.splitbill.repository;

import com.allobankdev.splitbill.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    List<Expense> findByBillGroupId(String groupId);
}
