package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "dapil")
@AllArgsConstructor
@NoArgsConstructor
public class Dapil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaDapil;

    private String provinsi;

    @ElementCollection
    private List<String> wilayahDapilList;

    private int jumlahKursi;
}
