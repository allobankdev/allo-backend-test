package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Data
public class Partai {
	@Id
	private UUID id;
	private String namaPartai;
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

}
