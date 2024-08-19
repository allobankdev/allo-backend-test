package com.allobank.allobackendtest.model;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "partai")
public class Partai {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String namaPartai;
    private Integer nomorUrut;
}
