package com.example.tech.splitbiller.repository;

import com.example.tech.splitbiller.entity.Expense;
import com.example.tech.splitbiller.entity.Group;
import com.example.tech.splitbiller.model.response.ExpenseCategorySummaryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    @Query("""
        SELECT new com.example.tech.splitbiller.model.response.ExpenseCategorySummaryResponse(
                e.expenseCategory.id,
                e.expenseCategory.name,
                SUM(e.amount)
        )
        FROM Expense e
        WHERE e.group.id = :groupId
        GROUP BY e.expenseCategory.id, e.expenseCategory.name
        ORDER BY SUM(e.amount) DESC
    """)
    List<ExpenseCategorySummaryResponse> summarizeByCategoryAndGroup(@Param("groupId") String groupId);

    @Query("""
        SELECT new com.example.tech.splitbiller.model.response.ExpenseCategorySummaryResponse(
                e.expenseCategory.id,
                e.expenseCategory.name,
                SUM(e.amount)
        )
        FROM Expense e
        GROUP BY e.expenseCategory.id, e.expenseCategory.name
        ORDER BY SUM(e.amount) DESC
    """)
    List<ExpenseCategorySummaryResponse> summarizeByCategory();

    @Query("""
        SELECT DISTINCT e
        FROM Expense e
        LEFT JOIN FETCH e.shares s
        LEFT JOIN FETCH s.person
        LEFT JOIN FETCH e.paidBy
        WHERE e.group = :group
    """)
    List<Expense> findAllForBalanceCalculation(@Param("group") Group group);

    @Query("""
        SELECT DISTINCT e
        FROM Expense e
        LEFT JOIN FETCH e.shares s
        LEFT JOIN FETCH s.person
        LEFT JOIN FETCH e.paidBy
        LEFT JOIN FETCH e.expenseCategory
        WHERE e.group = :group
    """)
    List<Expense> findAllForGroup(@Param("group") Group group);

    @Query("""
        SELECT DISTINCT e
        FROM Expense e
        LEFT JOIN FETCH e.shares s
        LEFT JOIN FETCH s.person
        LEFT JOIN FETCH e.paidBy
        LEFT JOIN FETCH e.expenseCategory
        WHERE e.id = :expenseId AND e.group = :group
    """)
    Optional<Expense> findForGroup(@Param("expenseId") String expenseId, @Param("group") Group group);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.group.id = :groupId
    """)
    BigDecimal calculateTotalExpenses(@Param("groupId") String groupId);
}
