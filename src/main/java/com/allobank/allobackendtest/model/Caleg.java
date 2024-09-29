package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "caleg")
public class Caleg {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "dapil",referencedColumnName = "id")
    private Dapil dapil;
    @ManyToOne
    @JoinColumn(name = "partai",referencedColumnName = "id")
    private Partai partai;
    @Column(name = "nomor_urut")
    private Integer nomorUrut;
    @Column(name = "nama")
    private String nama;
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;
}
