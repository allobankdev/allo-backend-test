package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "partai")
public class PartaiEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "nama_partai")
    private String namaPartai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;
}
