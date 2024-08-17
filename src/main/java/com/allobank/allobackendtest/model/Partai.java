package com.allobank.allobackendtest.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import jakarta.persistence.Entity;

@Data
@Entity
@Getter
@Setter
public class Partai {
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
