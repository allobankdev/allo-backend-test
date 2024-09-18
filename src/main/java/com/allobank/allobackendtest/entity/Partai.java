package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "partai")
public class Partai {

    @Id
    @SequenceGenerator(name="SEQ_PARTAI", sequenceName="partai_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PARTAI")
    @Column(name = "PARTAI_ID")
    private Long partaiId;

    @Column(nullable = false)
    private String namaPartai;

    @Column(nullable = false)
    private Integer nomorUrut;
}
