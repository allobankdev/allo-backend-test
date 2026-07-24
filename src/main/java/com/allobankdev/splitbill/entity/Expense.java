package com.allobankdev.splitbill.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private BillGroup billGroup;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String paidBy;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "expense_splits", joinColumns = @JoinColumn(name = "expense_id"))
    @Column(name = "participant_name")
    @Builder.Default
    private List<String> splitAmong = new ArrayList<>();
}
