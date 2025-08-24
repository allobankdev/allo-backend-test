package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wilayah_dapil")
public class WilayahDapil {

    @Id
    private UUID id;

    @Column(name = "wilayah_dapil", nullable = false)
    private String wilayahDapil;

    @ManyToOne
    @JoinColumn(name = "dapil_id", nullable = false)
    private Dapil dapil;

}

