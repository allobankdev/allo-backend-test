package com.allobank.allobackendtest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Partai {
    @Id
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
