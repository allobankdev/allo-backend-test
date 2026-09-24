package com.example.tech.splitbiller.repository;

import com.example.tech.splitbiller.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, String> {

    List<ExpenseCategory> findByDeleted(Boolean deleted);

    Optional<ExpenseCategory> findByIdAndDeleted(String id, Boolean deleted);
}
