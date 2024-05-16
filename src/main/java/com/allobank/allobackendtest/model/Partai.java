package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity(name = "partai")
public class Partai extends AbstractDate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
