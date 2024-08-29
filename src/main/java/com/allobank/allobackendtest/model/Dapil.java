package com.allobank.allobackendtest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dapil")
@Data
public class Dapil {
    @Id
    private UUID id;
    private String namaDapil;
    private String provinsi;
    private List<String> wilayahDapilList;
    private int jumlahKursi;
}
