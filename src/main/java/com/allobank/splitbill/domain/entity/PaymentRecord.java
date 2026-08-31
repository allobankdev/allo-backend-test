package com.allobank.splitbill.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore
    private BillGroup group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_participant_id", nullable = false)
    private Participant fromParticipant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_participant_id", nullable = false)
    private Participant toParticipant;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_at", nullable = false, updatable = false)
    private LocalDateTime paidAt;

    @Column
    private String notes;

    @PrePersist
    protected void onCreate() {
        this.paidAt = LocalDateTime.now();
    }
}
