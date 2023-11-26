package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "partai")
public class Partai {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name_partai")
    private String namaPartai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;
}
