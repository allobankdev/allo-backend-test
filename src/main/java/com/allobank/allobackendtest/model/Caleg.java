package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Caleg {
    @Id
    private String id;

    @ManyToOne
    private Dapil dapil;

    @ManyToOne
    private Partai partai;
    private Integer nomorUrut;
    private String nama;

    @Enumerated(EnumType.STRING)
    private JenisKelamin jenisKelamin;
}
