package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "partai")
public class PartaiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nama_partai")
    private String namaPartai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;

    @OneToMany(mappedBy = "partai")
    private List<CalegEntity> calegPartaiList;
}
