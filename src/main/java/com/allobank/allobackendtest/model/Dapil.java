package com.allobank.allobackendtest.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;

@Data
@Entity
@Getter
@Setter
public class Dapil {
    private UUID id;
    private String namaDapil;
    private String provinsi;
    private List<String> wilayahDapilList;
    private int jumlahKursi;
}
