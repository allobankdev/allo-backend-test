package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "Dapil")
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dapil_id")
    private UUID Id;
    private String namaDapil;
    private String provinsi;
    @ElementCollection
    private List<String> wilayahDapilList;
    private int jumlahKursi;
}
