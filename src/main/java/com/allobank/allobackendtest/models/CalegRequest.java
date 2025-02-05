package com.allobank.allobackendtest.models;

import java.util.UUID;

import com.allobank.allobackendtest.enums.JenisKelamin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalegRequest {
	private UUID dapilId;
	private UUID partaiId;
	private Integer nomorUrut;
	private String nama;
	private JenisKelamin jenisKelamin;
}
