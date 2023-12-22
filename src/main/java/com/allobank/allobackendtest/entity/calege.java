package com.allobank.allobackendtest.entity;
import java.util.List;
import java.util.UUID;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "caleg")
public class calege{
    @Id
    @GeneratedValue
    (strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn
    (name = "dapil_ID", nullable = false)
    private dapile dapil;

    @ManyToOne
    @JoinColumn
    (name = "partai_ID", nullable = false)
    private partaie partai;

    @Column
    (name = "nomor_urut")

    private Integer nomorUrut;

    private String nama;

    @Enumerated
    (EnumType.STRING)
    @Column
    (name = "jenis_kelamin", length = 20)
    private JenisKelamin jenisKelamin;
}