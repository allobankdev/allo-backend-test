package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "partai")
public class Partai {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nama", updatable = true, nullable = false)
    private String namaPartai;

    @Column(name = "nomor_urut", updatable = true, nullable = false)
    private Integer nomorUrut;

    @Column(name = "created_at", updatable = true, nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", updatable = true, nullable = true)
    private Timestamp updatedAt;

    @Column(name = "deleted_at", updatable = true, nullable = true)
    private Timestamp deletedAt;
}
