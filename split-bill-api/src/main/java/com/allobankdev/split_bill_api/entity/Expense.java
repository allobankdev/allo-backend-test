package com.allobankdev.split_bill_api.entity;

import com.allobankdev.split_bill_api.enums.SplitStrategy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "expenses")
public class Expense extends BaseEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private SplitStrategy splitStrategy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private BillGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by")
    private Participant paidBy;

    @OneToMany(mappedBy = "expense",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ExpenseSplit> splits = new ArrayList<>();
}
