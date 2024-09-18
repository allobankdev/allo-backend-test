package com.allobank.allobackendtest.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="caleg")
public class Caleg {
	@Id
    private UUID id;
	
	@ManyToOne
	@JoinColumn(name="nama_dapil",referencedColumnName = "nama_dapil")
    private Dapil dapil;
    
    @ManyToOne
    @JoinColumn(name="nama_partai",referencedColumnName = "nama_partai")
    private Partai partai;
    
    @Column(name="nomor_urut")
    private Integer nomorUrut;
    
    private String nama;
    
    @Enumerated(EnumType.STRING)
    @Column(name="jenis_kelamin")
    private JenisKelamin jenisKelamin;
    
    public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public Dapil getDapil() {
		return dapil;
	}
	public void setDapil(Dapil dapil) {
		this.dapil = dapil;
	}
	public Partai getPartai() {
		return partai;
	}
	public void setPartai(Partai partai) {
		this.partai = partai;
	}
	public Integer getNomorUrut() {
		return nomorUrut;
	}
	public void setNomorUrut(Integer nomorUrut) {
		this.nomorUrut = nomorUrut;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public JenisKelamin getJenisKelamin() {
		return jenisKelamin;
	}
	public void setJenisKelamin(JenisKelamin jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}
	
	public Caleg(UUID id, Dapil dapil, Partai partai, Integer nomorUrut, String nama, JenisKelamin jenisKelamin) {
		super();
		this.id = id;
		this.dapil = dapil;
		this.partai = partai;
		this.nomorUrut = nomorUrut;
		this.nama = nama;
		this.jenisKelamin = jenisKelamin;
	}
	public Caleg() {
	}
}
