package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Data
public class Dapil {
	@Id
	private UUID id;
	private String namaDapil;
	private String provinsi;
	private List<String> wilayahDapilList;
	private int jumlahKursi;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNamaDapil() {
		return namaDapil;
	}

	public void setNamaDapil(String namaDapil) {
		this.namaDapil = namaDapil;
	}

	public String getProvinsi() {
		return provinsi;
	}

	public void setProvinsi(String provinsi) {
		this.provinsi = provinsi;
	}

	public List<String> getWilayahDapilList() {
		return wilayahDapilList;
	}

	public void setWilayahDapilList(List<String> wilayahDapilList) {
		this.wilayahDapilList = wilayahDapilList;
	}

	public int getJumlahKursi() {
		return jumlahKursi;
	}

	public void setJumlahKursi(int jumlahKursi) {
		this.jumlahKursi = jumlahKursi;
	}

}
