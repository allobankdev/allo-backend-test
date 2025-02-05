package com.allobank.allobackendtest.entities;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Dapil {
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
	private String namaDapil;
	private String provinsi;
	private List<String> wilayahDapilList;
	private int jumlahKursi;
}
