package com.allobank.allobackendtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalegResponse {

	private String id;

	private String nama;

	private String nomor_urut;

	private String dapil_id;

	private String nama_dapil;

	private String provinsi;

	private String wilayah_dapil;

	private String partai_id;

	private String nama_partai;

	private String jenisKelamin;
}
