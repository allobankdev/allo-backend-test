package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "dapil")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Dapil {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)

    private String namaDapil;
    private String provinsi;

    private String wilayahDapilList;

    private int jumlahKursi;
}
