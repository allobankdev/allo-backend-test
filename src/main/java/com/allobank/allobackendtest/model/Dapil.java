package com.allobank.allobackendtest.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "dapil", schema = "pemilu")
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nama_dapil", nullable = false)
    private String namaDapil;

    @Column(nullable = false)
    private String provinsi;

    @Column(name = "wilayah_dapil_list", columnDefinition = "text")
    private String wilayahDapilList; // Store as JSON string

    @Column(name = "jumlah_kursi", nullable = false)
    private int jumlahKursi;

    @Transient
    public List<String> getWilayahDapilListAsList() {
        // Convert JSON string to List when needed
        if (wilayahDapilList == null) return List.of();
        try {
            return List.of(wilayahDapilList
                    .replace("[", "")
                    .replace("]", "")
                    .replace("\"", "")
                    .split(","));
        } catch (Exception e) {
            return List.of();
        }
    }
}