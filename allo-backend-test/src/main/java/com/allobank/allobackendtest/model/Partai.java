package com.allobank.allobackendtest.model;

public class Partai {
    private Long id;
    private String nama;

    // Constructor dengan parameter
    public Partai(Long id, String nama) {
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