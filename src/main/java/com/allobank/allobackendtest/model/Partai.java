package com.allobank.allobackendtest.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "partai")
@Data
public class Partai {
    @Id
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
