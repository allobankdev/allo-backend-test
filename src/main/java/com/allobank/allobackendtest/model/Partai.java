package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "Partai")
public class Partai {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "partai_id")
    private UUID Id;
    private String namaPartai;
    private Integer nomorUrut;
}
