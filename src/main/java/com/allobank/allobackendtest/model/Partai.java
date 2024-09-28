package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Partai {
    @Id
    private String id;
    private String namaPartai;
    private Integer nomorUrut;
}
