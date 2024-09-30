package com.allobank.allobackendtest.model;

import lombok.*;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "DAPIL")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String namaDapil;
    private String provinsi;

    @ElementCollection
    private List<String> wilayahDapilList;

    private int jumlahKursi;
}
