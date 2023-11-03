package com.allobank.allobackendtest.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCalegRequest {

	private String dapil_id;

	private String partai_id;

	private String nomor_urut;

	private String jenisKelamin;

	@NotNull
	private Integer page;

	@NotNull
	private Integer size;
}
