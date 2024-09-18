package com.allobank.allobackendtest.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="partai")
public class Partai {
	@Id
    private UUID id;
	
	@Column(name="nama_partai")
    private String namaPartai;
    
	@Column(name="nomor_urut")
    private Integer nomorUrut;
	
    public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getNamaPartai() {
		return namaPartai;
	}
	public void setNamaPartai(String namaPartai) {
		this.namaPartai = namaPartai;
	}
	public Integer getNomorUrut() {
		return nomorUrut;
	}
	public void setNomorUrut(Integer nomorUrut) {
		this.nomorUrut = nomorUrut;
	}
	
	public Partai(UUID id, String namaPartai, Integer nomorUrut) {
		super();
		this.id = id;
		this.namaPartai = namaPartai;
		this.nomorUrut = nomorUrut;
	}
	
	public Partai() {
	}
}
