package com.allobank.allobackendtest.entity;

import com.allobank.allobackendtest.model.JenisKelamin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "caleg")
public class CalegEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dapil_id", nullable = false)
    @JsonIgnoreProperties({"calegs"})
    private DapilEntity dapil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partai_id", nullable = false)
    @JsonIgnoreProperties({"calegList"})
    private PartaiEntity partai;

    @Column(name = "nomor_urut", nullable = false)
    private Integer nomorUrut;

    @Column(nullable = false, length = 100)
    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin", nullable = false)
    private JenisKelamin jenisKelamin;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
