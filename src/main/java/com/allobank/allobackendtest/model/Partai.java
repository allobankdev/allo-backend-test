package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Data
public class Partai {
    @Id
    @GeneratedValue
    private UUID id;

    private String namaPartai;
    
    private Integer nomorUrut;
}
