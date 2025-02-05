package com.allobank.allobackendtest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartaiRequest {
	private String namaPartai;
	private Integer nomorUrut;
}
