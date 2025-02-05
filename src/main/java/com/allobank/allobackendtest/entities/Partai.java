package com.allobank.allobackendtest.entities;

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
public class Partai {
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
	private String namaPartai;
	private Integer nomorUrut;
}
