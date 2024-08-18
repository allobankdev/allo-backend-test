package com.allobank.allobackendtest.model;

public class Dapil {
    private Long id;
    private String nama;

    // Constructor dengan parameter
    public Dapil(Long id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
