package com.allobank.allobackendtest.model;

import lombok.*;

import java.util.UUID;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "CALEG")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Caleg {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_dapil", nullable = false)
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "id_partai", nullable = false)
    private Partai partai;

    private Integer nomorUrut;
    private String nama;

    @Enumerated(EnumType.STRING)
    private JenisKelamin jenisKelamin;
}
