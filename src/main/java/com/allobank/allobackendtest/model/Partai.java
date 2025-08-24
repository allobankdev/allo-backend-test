package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "party")
@AllArgsConstructor
@NoArgsConstructor
public class Partai {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", length = 50, nullable = false)
    private String namaPartai;

    @Column(name = "order_number", nullable = false)
    private Integer nomorUrut;
}
