package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "partai")
public class Partai {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="nama_partai", nullable = false)
    private String namaPartai;

    @Column(name="nomo_urut", nullable = false)
    private Integer nomorUrut;
}
