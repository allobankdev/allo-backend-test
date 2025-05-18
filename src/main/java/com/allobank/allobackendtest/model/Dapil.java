package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "dapil")
public class Dapil {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String namaDapil;
    private String provinsi;
    private List<String> wilayahDapilList;
    private int jumlahKursi;
}
