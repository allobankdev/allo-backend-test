package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class Caleg {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dapil_id")
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id")
    private Partai partai;
    private Integer nomorUrut;
    private String nama;

    @Enumerated(EnumType.STRING)
    private JenisKelamin jenisKelamin;
}
