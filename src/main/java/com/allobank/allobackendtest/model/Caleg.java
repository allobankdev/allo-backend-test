package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
@Data
public class Caleg {
	@Id
	private UUID id;

	@OneToOne
	private Dapil dapil;

	@OneToOne
	private Partai partai;
	private Integer nomorUrut;
	private String nama;
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

}
