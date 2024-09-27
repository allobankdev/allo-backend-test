package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class Partai {
    @Id
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
