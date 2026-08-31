package com.allobank.splitbill.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_splits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    @JsonIgnore
    private Expense expense;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @Column(name = "share_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal shareAmount;

    @Column(name = "share_percentage", precision = 5, scale = 2)
    private BigDecimal sharePercentage;
}
