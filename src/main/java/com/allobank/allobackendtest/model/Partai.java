package com.allobank.allobackendtest.model;

import lombok.*;

import jakarta.persistence.*;

import java.util.UUID;

@Data
@Entity
@Table(name = "PARTAI")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Partai {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String namaPartai;
    private Integer nomorUrut;
}
