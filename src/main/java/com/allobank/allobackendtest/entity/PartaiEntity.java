package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "partai")
public class PartaiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nama_partai", nullable = false, unique = true)
    private String namaPartai;

    @Column(name = "nomor_urut", nullable = false, unique = true)
    private Integer nomorUrut;
}

