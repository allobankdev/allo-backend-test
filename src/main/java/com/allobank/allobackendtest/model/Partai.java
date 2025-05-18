package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "partai")
public class Partai {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
