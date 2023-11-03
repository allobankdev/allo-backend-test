package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "caleg")
public class Caleg {
	@Id
	private String id;

	private String nama;

	private String nomor_urut;

	private String jenisKelamin;

	@Column(insertable=false, updatable=false, name = "dapil_id")
	private String dapil_id;

	@Column(insertable=false, updatable=false, name = "partai_id")
	private String partai_id;

	@ManyToOne()
	@JoinColumn(name = "dapil_id")
	private Dapil dapil;

	@ManyToOne()
	@JoinColumn(name = "partai_id")
	private Partai partai;
}
