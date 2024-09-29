package com.allobank.allobackendtest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "partai")
public class Partai {
    @Id
    private UUID id;
    @Column(name = "nama_partai")
    private String namaPartai;
    @Column(name = "nomor_urut")
    private Integer nomorUrut;
}
