package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Dapil {
    @Id
    private String id;
    private String namaDapil;
    private String provinsi;
    @ElementCollection
    private List<String> wilayahDapilList;
    private int jumlahKursi;
}
