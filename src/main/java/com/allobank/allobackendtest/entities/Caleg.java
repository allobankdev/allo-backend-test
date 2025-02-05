package com.allobank.allobackendtest.entities;

import java.util.UUID;

import com.allobank.allobackendtest.enums.JenisKelamin;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Caleg {
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
	@ManyToOne
	@JoinColumn(name = "dapil_id", nullable = false)
	private Dapil dapil;
	@ManyToOne
	@JoinColumn(name = "partai_id", nullable = false)
	private Partai partai;
	private Integer nomorUrut;
	private String nama;
	@Enumerated(EnumType.STRING)
	private JenisKelamin jenisKelamin;
}
