package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "dapil")
public class Dapil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nama_dapil", nullable = false)
    private String namaDapil;

    @Column(name = "wilayah", nullable = true)
    private String wilayah;

    @Column(name = "alokasi_kursi", nullable = false)
    private int alokasiKursi;


    @Column(name = "created_at", nullable = false)
    private Date createdAt;

}