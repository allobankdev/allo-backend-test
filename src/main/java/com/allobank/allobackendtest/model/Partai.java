package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "partai")
public class Partai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nama_partai", nullable = false)
    private String namaPartai;

    @Column(name = "singkatan_partai", nullable = true)
    private String singkatanPartai;


    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}