package com.allobank.allobackendtest.entity;
import java.util.UUID;

import java.util.List;

import jakarta.persistence.Column;
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
@Table
(name = "partai")
public class partaie{

    @Id
    @GeneratedValue
    (strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    (name = "nama_partai")
    private String namaPartai;

    @Column
    (name = "nomor_urut")
    private Integer nomorUrut;

    @OneToMany
    (mappedBy = "partai")
    private List<calege> calegPartaiList;
}