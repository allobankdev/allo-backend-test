package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "legislative")
@AllArgsConstructor
@NoArgsConstructor
public class Caleg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "electoral_district_id", nullable = false)
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    private Partai partai;

    @Column(name = "order_number", nullable = false)
    private Integer nomorUrut;

    @Column(name = "name", length = 50, nullable = false)
    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private JenisKelamin jenisKelamin;
}
