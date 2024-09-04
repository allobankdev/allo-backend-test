package com.allobank.allobackendtest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "partai")
@Data
public class Partai {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nama_partai")
    private String namaPartai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;
}
