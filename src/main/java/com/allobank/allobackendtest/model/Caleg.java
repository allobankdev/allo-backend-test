package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "caleg")
@AllArgsConstructor
@NoArgsConstructor
public class Caleg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dapil_id", nullable = false)
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id", nullable = false)
    private Partai partai;

    private Integer nomorUrut;

    private String nama;

    @Enumerated(EnumType.STRING)
    private JenisKelamin jenisKelamin;
}
