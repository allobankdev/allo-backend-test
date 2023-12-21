package com.allobank.allobackendtest.model;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

@Entity
@Table(name = "partai")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Partai {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private  String id;
    @Column(nullable = false)
    private String namaPartai;
    private Integer nomorUrut;

}
