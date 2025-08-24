package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "partai")
public class Partai {

    @Id
    private UUID id;

    @Column(name = "nama_partai", nullable = false)
    private String namaPartai;

    @Column(name = "nomor_urut", nullable = false)
    private Integer nomorUrut;

    @OneToMany(mappedBy = "partai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Caleg> calegList;
}
