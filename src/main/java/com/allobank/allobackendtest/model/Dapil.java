package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Data
public class Dapil {
    @Id
    @GeneratedValue
    private UUID id;

    private String namaDapil;

    private String provinsi;

    @ElementCollection
    private List<String> wilayahDapilList;
    
    private int jumlahKursi;
}
