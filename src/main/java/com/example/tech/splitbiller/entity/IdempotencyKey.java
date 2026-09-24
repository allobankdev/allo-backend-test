package com.example.tech.splitbiller.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "`idempotency_keys`")
public class IdempotencyKey {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    private String idempotencyKey;
    private String hashRequest;
    private String response;
    private Long createdAt;

    @PrePersist
    private void generateId() {
        if (id == null) {
            id = UuidCreator.getTimeOrderedEpoch().toString();
        }
    }
}
