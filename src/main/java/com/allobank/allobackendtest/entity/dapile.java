package com.allobank.allobackendtest.entity;
import java.util.UUID;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dapil")
public class dapile{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nama_dapil")
    private String namaDapil;

    private String provinsi;

    @Column(name = "jumlah_kursi")
    private Integer jumlahKursi;

    @Column(name = "wilayah_dapil")
    @ElementCollection
    private List<String> wilayahDapilList;

    @OneToMany(mappedBy = "dapil")
    private List<calege> calegDapiList;
}