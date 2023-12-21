package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "caleg")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Caleg {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    @ManyToOne
    @JoinColumn(name = "dapil_id")
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id")
    private Partai partai;


    @Column(nullable = false)
    private Integer nomorUrut;

    @Column(nullable = false)
    private String nama;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JenisKelamin jenisKelamin;
}
