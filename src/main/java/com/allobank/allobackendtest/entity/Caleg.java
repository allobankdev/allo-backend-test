package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "caleg")
public class Caleg {

    @Id
    @SequenceGenerator(name="SEQ_CALEQ", sequenceName="caleg_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CALEQ")
    @Column(name = "CALEG_ID")
    private Long calegId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dapil_id", nullable = false)
    private Dapil dapil;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partai_id", nullable = false)
    private Partai partai;

    @Column(nullable = false)
    private Integer nomorUrut;

    @Column(nullable = false)
    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JenisKelamin jenisKelamin;
}
