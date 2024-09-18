package com.allobank.allobackendtest.model;

import java.util.List;
import java.util.UUID;

import com.allobank.allobackendtest.converter.ListStringConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="dapil")
public class Dapil {
	@Id
    private UUID id;
	
	@Column(name="nama_dapil")
    private String namaDapil;
    
	private String provinsi;
    
	@Convert(converter = ListStringConverter.class)
    @Column(name="wilayah_dapil_list")
    private List<String> wilayahDapilList;
    
    @Column(name="jumlah_kursi")
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
	
	public Dapil(UUID id, String namaDapil, String provinsi, List<String> wilayahDapilList, int jumlahKursi) {
		super();
		this.id = id;
		this.namaDapil = namaDapil;
		this.provinsi = provinsi;
		this.wilayahDapilList = wilayahDapilList;
		this.jumlahKursi = jumlahKursi;
	}
	public Dapil() {
	}
}
