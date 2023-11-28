package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "caleg")
public class Caleg {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dapil_id", updatable = true, referencedColumnName = "id")
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id", updatable = true, referencedColumnName = "id")
    private Partai partai;

    @Column(name = "nomor_urut", updatable = true, nullable = false)
    private Integer nomorUrut;

    @Column(name = "nama", updatable = true, nullable = false)
    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin", updatable = true, nullable = false)
    private JenisKelamin jenisKelamin;

    @Column(name = "created_at", updatable = true, nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", updatable = true, nullable = true)
    private Timestamp updatedAt;

    @Column(name = "deleted_at", updatable = true, nullable = true)
    private Timestamp deletedAt;

}
